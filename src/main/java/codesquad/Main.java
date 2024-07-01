package codesquad;

import codesquad.handler.ConnectionHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final int PORT = 8080;
    private static final int CORE_POOL_SIZE = 10;
    private static final int MAX_POOL_SIZE = 20;
    private static final long KEEP_ALIVE_TIME = 0;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT); // 8080 포트에서 서버를 엽니다.
        log.debug("Listening for connection on port 8080 ....");
        var executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
            KEEP_ALIVE_TIME, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>());
        while (true) {
            executor.execute(new ConnectionHandler(serverSocket.accept()));
        }
    }

}
