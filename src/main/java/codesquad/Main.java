package codesquad;

import codesquad.webserver.RequestHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    public static void main(String[] args) throws IOException {
        Logger log = LoggerFactory.getLogger(Main.class);
        ServerSocket serverSocket = new ServerSocket(8080); // 8080 포트에서 서버를 엽니다.
        log.info("Listening for connection on port 8080 ....");

        while (true) { // 무한 루프를 돌며 클라이언트의 연결을 기다립니다.
            try (Socket clientSocket = serverSocket.accept()) { // 클라이언트 연결을 수락합니다.
                RequestHandler handler = new RequestHandler(clientSocket);
                handler.handle();
            }
        }
    }
}
