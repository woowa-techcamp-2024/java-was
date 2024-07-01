package codesquad;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
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

        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 0, TimeUnit.MICROSECONDS,
                new LinkedBlockingQueue<>());

        while (true) { // 무한 루프를 돌며 클라이언트의 연결을 기다립니다.
            executor.execute(new Connection(serverSocket.accept()));
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
