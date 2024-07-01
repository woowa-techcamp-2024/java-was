package codesquad;

import codesquad.http.request.HttpRequest;
import codesquad.http.request.HttpRequestParser;
import codesquad.http.response.HttpResponse;
import codesquad.http.response.HttpResponseSender;
import codesquad.http.urlMapper.ResourceMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static codesquad.http.response.HttpResponseSender.*;
import static codesquad.http.urlMapper.ResourceGetter.*;


public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final ResourceMapping resourceMapping = new ResourceMapping();
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080); // 8080 포트에서 서버를 엽니다.
        while (true) { // 무한 루프를 돌며 클라이언트의 연결을 기다립니다.
            try (Socket clientSocket = serverSocket.accept()) { // 클라이언트 연결을 수락합니다.

                InputStream inputStream = clientSocket.getInputStream();
                log.debug(inputStream.toString());
                HttpRequest request = HttpRequestParser.parseHttpRequest(inputStream);
                log.debug(request.toString());


                // HTTP 응답을 생성합니다.
                OutputStream clientOutput = clientSocket.getOutputStream();
                String resourcePath = "";

                String url = request.getUrl();
                if(url.startsWith("/img")){
                    resourcePath = "/static"+ url;
                }
                else {
                    resourcePath = resourceMapping.getResourcePath(url);
                }

                System.out.println(resourcePath);

                // 요청된 URL과 매핑된 리소스 파일 경로 가져오기
                if (resourcePath != null) {
                    if (resourcePath.endsWith(".html")) {
                        sendHtmlResponse(clientSocket.getOutputStream(), "200 OK", "text/html", getResourceBytes(resourcePath));
                    } else if (resourcePath.endsWith(".css")) {
                        sendHttpResponse(clientSocket.getOutputStream(), "200 OK", "text/css", getResourceBytes(resourcePath));
                    } else if (resourcePath.endsWith(".js")) {
                        sendHttpResponse(clientSocket.getOutputStream(), "200 OK", "application/javascript", getResourceBytes(resourcePath));
                    } else if (resourcePath.endsWith(".jpg") || resourcePath.endsWith(".png") || resourcePath.endsWith(".svg")) {
                        sendImageResponse(clientSocket.getOutputStream(), "200 OK", getContentType(resourcePath), getResourceBytes(resourcePath));
                    }
                    System.out.println(resourcePath);

                    // HTTP 응답 보내기
                    sendHttpResponse(clientOutput, "200 OK", getContentType(resourcePath), getResourceBytes(resourcePath));
                } else {
                    // 요청된 리소스가 없음 (404 Not Found)
                    sendHttpResponse(clientOutput, "404 Not Found", "text/plain", "Resource not found".getBytes(StandardCharsets.UTF_8));
                }
                clientOutput.flush();
            }
        }
    }
}
