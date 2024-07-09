package codesquad;

import codesquad.handler.HandlersMapper;
import codesquad.http.HttpRequestReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static final int MAXIMUM_THREAD_POOL_SIZE = 10;

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(MAXIMUM_THREAD_POOL_SIZE);
        HandlersMapper handlersMapper = new HandlersMapper();
        HttpRequestReader requestReader = new HttpRequestReader();
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            log.debug("Listening for connection on port 8080 ....");
            while (true) {
                executorService.execute(new HttpRequestProcessor(serverSocket.accept(), requestReader, handlersMapper));
            }
        }
    }

}
