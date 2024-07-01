package codesquad.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private final ExecutorService threadPool;
    private final int port;

    public WebServer(int port, int poolSize) {
        this.threadPool = Executors.newFixedThreadPool(poolSize);
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Starting web server on port {}", port);
            while (!Thread.currentThread().isInterrupted()) {
                //TODO: 커넥션을 accept하는것부터 스레드를 분리하는게 맞나?
                threadPool.submit(() -> handleConnections(serverSocket));
            }
        } catch (IOException e) {
            logger.error("Error while starting web server", e);
        }
    }

    private void handleConnections(ServerSocket serverSocket) {
        try {
            Socket client = serverSocket.accept();
            handleRequest(client);
        } catch (IOException e) {
            logger.error("Error while handling connections", e);
        }
    }

    private void handleRequest(Socket client) {
        try {
            new RequestHandler().handle(client);
        } catch (Exception e) {
            logger.error("Error while handling request", e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                logger.error("Error while closing connection", e);
            }
        }
    }

    public static void main(String[] args) {
        int port = 8080;
        int threadPoolSize = 10;
        new WebServer(port, 10).start();
    }
}
