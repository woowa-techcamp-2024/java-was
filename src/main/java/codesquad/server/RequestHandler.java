package codesquad.server;

import codesquad.authenticate.Authenticator;
import codesquad.handler.Handler;
import codesquad.handler.HandlerMapper;
import codesquad.http.request.HttpRequest;
import codesquad.http.request.HttpRequestParser;
import codesquad.http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
//    private final Socket clientSocket;
    private final HttpRequestParser httpRequestParser;
    private final Socket clientSocket;

    public RequestHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.httpRequestParser = new HttpRequestParser();
    }

    @Override
    public void run() {

        try {
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            // HttpRequest 클래스로 파싱하는 부분
            HttpRequest request = httpRequestParser.parse(inputStream);
            // log.info("{}", request.toString());
//            log.info("Request Method : {}", request.method());
//            log.info("Request URI : {}", request.path());
//            log.info("Request Body: {}\n", new String(request.body()));

            if(!Authenticator.auth(request)) {
                log.info("인증 실패");
                writeResponse(outputStream, new HttpResponse("/login"));
                return;
            }

            // HandlerMapper 이용하여 요청을 처리할 수 있는 Handler 찾고 요청 handling
            Handler handler = HandlerMapper.findHandler(request.path());
            if (handler == null) {
                writeResponse(outputStream, HttpResponse.notFound());
                close();
                return;
            }
            HttpResponse response = handler.handle(request);

            // HttpResponse 인스턴스를 반환하는 부분
            writeResponse(outputStream, response);
            close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    private void writeResponse(OutputStream outputStream, HttpResponse response) throws IOException {
        outputStream.write(response.toByteArray());
        outputStream.write(response.getBody());
    }

    private void close() throws IOException {
        clientSocket.close();
    }
}
