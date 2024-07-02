package codesquad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;


public class Main {
    private static final int THREAD_COUNT = 10;
    public static void main(String[] args) throws IOException {
        final Logger log = LoggerFactory.getLogger(Main.class);
        ServerSocket serverSocket = new ServerSocket(8080); // 8080 포트에서 서버를 엽니다.
        log.info("Listening for connection on port {} ....", serverSocket.getLocalPort());

        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(new SocketAccepter(serverSocket)).start();
        }
    }
}
