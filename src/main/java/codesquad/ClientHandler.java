package codesquad;

import http.Http;
import http.startline.RequestLine;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import util.FileReader;
import util.LoggerUtil;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final Logger logger = LoggerUtil.getLogger();

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
            OutputStream clientOutput = clientSocket.getOutputStream();
            InputStream inputStream = clientSocket.getInputStream()
        ) {
            // HTTP 응답을 생성합니다.
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Http http = Http.generate(bufferedReader);
            RequestLine requestLine = (RequestLine) http.getStartLine();
            logger.info("Request: {}", http);
            // resource/static/index.html 파일을 읽어서 보내기
            // 핸들러는 매칭되는 것만 찾고 파일은 핸들러와 무관하다. 일단은 핸들러가 작동하지 않는다고 가정하고 코드를 짠다.

            byte[] fileContent = FileReader.readFileFromUrlPath(requestLine.getUrlPath());
            String contentType = FileReader.guessContentTypeFromUrlPath(requestLine.getUrlPath());


            // HTTP 응답 헤더 생성
            String httpResponseHeader = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + fileContent.length + "\r\n" +
                "\r\n";
            clientOutput.write(httpResponseHeader.getBytes());
            clientOutput.write(fileContent);
            clientOutput.flush();

            //clientOutput
        } catch (FileNotFoundException e) {
            logger.error("File not found: {}", e.getMessage());
        } catch (IOException e) {
            logger.error("Error handling client: {}", e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.error("Error closing client socket: {}", e.getMessage());
            }
        }
    }
}
