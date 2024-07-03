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
            serverSocket.setReuseAddress(true); // 작업 중 껐다 켰다 하는 상황에 발생하는 포트 사용중 발생을 피하기 위해 임시 작성
            logger.info("Starting web server on port {}", port);
            while (!Thread.currentThread().isInterrupted()) {
                Socket client = serverSocket.accept();
                threadPool.submit(() -> handleRequest(client));
            }
        } catch (IOException e) {
            logger.error("Error while starting web server", e);
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
        new WebServer(port, threadPoolSize).start();
    }
}
