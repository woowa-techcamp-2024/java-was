package codesquad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080); // 8080 포트에서 서버를 엽니다.
        logger.debug("Listening for connection on port 8080 ....");

        while (true) { // 무한 루프를 돌며 클라이언트의 연결을 기다립니다.
            try (Socket clientSocket = serverSocket.accept();
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
            ) { // 클라이언트 연결을 수락합니다.
                String request = in.readLine();
                logger.debug("Client connected");
                if (!request.isEmpty()) {
                    String[] requestParts = request.split(" ");
                    logger.debug("Client request HTTP method: {}", requestParts[0]);
                    logger.debug("Client request URL: {}", requestParts[1]);
                    logger.debug("Client request HTTP Protocol: {}", requestParts[2]);
                }
                // HTTP 응답을 생성합니다.
                OutputStream clientOutput = clientSocket.getOutputStream();
                clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
                clientOutput.write("Content-Type: text/html\r\n".getBytes());
                clientOutput.write("\r\n".getBytes());
                clientOutput.write("<h1>Hello</h1>\r\n".getBytes()); // 응답 본문으로 "Hello"를 보냅니다.
                clientOutput.flush();
            }
        }
    }
}
