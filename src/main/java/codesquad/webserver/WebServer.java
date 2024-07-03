package codesquad.webserver;

import codesquad.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {
    private final Logger log = LoggerFactory.getLogger(Main.class);
    private ExecutorService executorService;
    private ServerSocket serverSocket;

    public void setUp(int port) {
        try{
            int n_THREADS = 10;
            executorService = Executors.newFixedThreadPool(n_THREADS);
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        while (true) {
            try{
                Socket clientSocket = serverSocket.accept();
                log.info("Listening for connection on port 8080 ....");
                executorService.submit(new RequestHandler(clientSocket));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
