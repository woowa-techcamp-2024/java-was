package codesquad;

import filter.FilterChain;
import http.HttpRequest;
import http.HttpResponse;
import http.startline.ResponseLine;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import util.LoggerUtil;

public class ClientThread implements Runnable {

    private final Socket clientSocket;
    private static final Logger logger = LoggerUtil.getLogger();

    public ClientThread(Socket clientSocket) {
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
            HttpRequest httpRequest = HttpRequest.generateHttpRequest(bufferedReader);
            HttpResponse httpResponse = HttpResponse.generateHttpResponse();

            // 여기까지가 response나 requets를 받는 부분

            //filter 매핑하기
            new FilterChain().doFilter(httpRequest, httpResponse);

            sendResponse(clientOutput, httpResponse);

        } catch (FileNotFoundException e) {
            logger.error("File not found: {}", e.getMessage());
        } catch (IOException e) {
            logger.error("Error handling client: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.error("Error closing client socket: {}", e.getMessage());
            }
        }
    }

    private void sendResponse(OutputStream clientOutput, HttpResponse httpResponse)
        throws IOException {
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        String responseHeaderLine = httpResponse.getHeader().toString();
        String body = new String(httpResponse.getBody());

        // logger.info("ResponseLine\n{}", responseLine);
        // logger.info("Response Header\n{}", responseHeaderLine);
        // logger.info("Response Body\n{}", body);

        clientOutput.write(responseLine.toString().getBytes());
        clientOutput.write(responseHeaderLine.getBytes());
        clientOutput.write("\r\n".getBytes());
        clientOutput.write(httpResponse.getBody());
        clientOutput.flush();
    }
}
