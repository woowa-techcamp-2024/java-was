package codesquad;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080); // 8080 포트에서 서버를 엽니다.
        log.debug("Listening for connection on port 8080 ....");

        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 0, TimeUnit.MICROSECONDS,
                new LinkedBlockingQueue<>());

        while (true) { // 무한 루프를 돌며 클라이언트의 연결을 기다립니다.
            executor.execute(new ConnectionHandler(serverSocket.accept()));
        }
    }
}
