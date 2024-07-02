package codesquad;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionHandler implements Runnable {

    private final Logger log = LoggerFactory.getLogger(Main.class);

    private final Socket clientSocket;

    public ConnectionHandler(final Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader requestReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream clientOutput = clientSocket.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream("src/main/resources/static/index.html")) {

            String httpRequestInformation = parseRequest(requestReader);

            log.debug(httpRequestInformation);
            log.debug("Client connected");

            // HTTP 응답을 생성합니다.
            clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
            clientOutput.write("Content-Type: text/html\r\n".getBytes());
            clientOutput.write("\r\n".getBytes());
            clientOutput.write(fileInputStream.readAllBytes()); // 응답 본문으로 "Hello"를 보냅니다.
            clientOutput.flush();
        } catch (IOException e) {
            log.error("요청을 처리할 수 없습니다.");
            throw new RuntimeException("요청을 처리할 수 없습니다.", e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                log.error("클라이언트 소켓을 닫을 수 없습니다.");
                throw new RuntimeException(e);
            }
        }
    }

    private String parseRequest(final BufferedReader requestReader) throws IOException {
        StringBuilder httpRequestBuilder = new StringBuilder();
        String requestLine;
        while (!(requestLine = requestReader.readLine()).isEmpty()) {
            httpRequestBuilder.append(requestLine)
                    .append(System.lineSeparator());
        }

        return httpRequestBuilder.toString();
    }
}
