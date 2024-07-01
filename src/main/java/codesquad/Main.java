package codesquad;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080); // 8080 포트에서 서버를 엽니다.
        System.out.println("Listening for connection on port 8080 ....");

        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 1000L, TimeUnit.MICROSECONDS,
                new LinkedBlockingQueue<>());

        while (true) { // 무한 루프를 돌며 클라이언트의 연결을 기다립니다.
            executor.execute(() -> {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader requestReader = new BufferedReader(
                             new InputStreamReader(clientSocket.getInputStream()));
                     OutputStream clientOutput = clientSocket.getOutputStream();
                     FileInputStream fileInputStream = new FileInputStream("src/main/resources/static/index.html")
                ) { // 클라이언트 연결을 수락합니다.

                    String httpRequest = printRequest(requestReader);
                    log.debug(httpRequest);

                    System.out.println("Client connected");

                    // HTTP 응답을 생성합니다.
                    clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
                    clientOutput.write("Content-Type: text/html\r\n".getBytes());
                    clientOutput.write("\r\n".getBytes());
                    clientOutput.write(fileInputStream.readAllBytes()); // 응답 본문으로 "Hello"를 보냅니다.
                    clientOutput.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private static String printRequest(final BufferedReader requestReader) throws IOException {
        StringBuilder httpRequestBuilder = new StringBuilder();
        String requestLine;
        while (!(requestLine = requestReader.readLine()).isEmpty()) {
            httpRequestBuilder.append(requestLine)
                    .append(System.lineSeparator());
        }

        return httpRequestBuilder.toString();
    }
}
