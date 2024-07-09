package codesquad;

import codesquad.handler.HandlersMapper;
import codesquad.handler.RequestHandler;
import codesquad.http.HttpHeaders;
import codesquad.http.HttpRequest;
import codesquad.http.HttpRequestReader;
import codesquad.http.HttpResponse;
import codesquad.http.parser.HttpRequestParser;
import codesquad.http.parser.ParsersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HttpRequestProcessor implements Runnable {

    private final Logger log = LoggerFactory.getLogger(HttpRequestProcessor.class);

    private final Socket socket;
    private final HttpRequestParser requestParser = ParsersFactory.getHttpRequestParser();
    private final HttpRequestReader requestReader;
    private final HandlersMapper handlersMapper;

    public HttpRequestProcessor(Socket socket, HttpRequestReader requestReader, HandlersMapper handlersMapper) {
        this.socket = socket;
        this.requestReader = requestReader;
        this.handlersMapper = handlersMapper;
        log.debug("Client connected");
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String requestText = requestReader.read(reader);
            HttpRequest httpRequest = requestParser.parse(requestText);
            if (!httpRequest.method().equals("GET")) {
                String headerValue = httpRequest.firstHeaderValue(HttpHeaders.CONTENT_LENGTH)
                        .orElseThrow(() -> new IllegalArgumentException("[ERROR] Content-Length가 누락되었습니다."));
                int contentLength = Integer.parseInt(headerValue);
                String requestBody = requestReader.readBody(reader, contentLength);
                httpRequest.setBody(requestBody);
            }
            log.info("httpRequest = {}", httpRequest);

            RequestHandler requestHandler = handlersMapper.getRequestHandler(httpRequest);
            HttpResponse httpResponse = requestHandler.handle(httpRequest);
            OutputStream clientOutput = socket.getOutputStream();
            clientOutput.write(httpResponse.toBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
