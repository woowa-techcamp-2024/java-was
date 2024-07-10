package codesquad.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connector {

    public static final int PORT = 8080;
    private static final Logger logger = LoggerFactory.getLogger(Connector.class);

    private final HttpProcessor httpProcessor;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public Connector(HttpProcessor httpProcessor) {
        this.httpProcessor = httpProcessor;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.debug("Listening for connection on port 8080 ....");
            run(serverSocket);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void run(ServerSocket serverSocket) {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executorService.execute(() -> httpProcessor.process(socket));
            } catch (IOException | RuntimeException e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }
    }

}
