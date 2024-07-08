package codesquad.webserver;

import static codesquad.utils.string.StringUtils.CRLF;

import codesquad.servlet.handler.HttpRequestHandler;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpRequestParser;
import codesquad.webserver.http.HttpResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpProcessor {

    private static final Logger logger = LoggerFactory.getLogger(HttpProcessor.class);

    private final HttpRequestParser httpRequestParser;
    private final HttpRequestHandler httpRequestHandler;
    private final Socket socket;

    public HttpProcessor(HttpRequestParser httpRequestParser, HttpRequestHandler httpRequestHandler, Socket socket) {
        this.httpRequestParser = httpRequestParser;
        this.httpRequestHandler = httpRequestHandler;
        this.socket = socket;
    }

    public void process() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()
        ) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            HttpRequest httpRequest = httpRequestParser.parse(bufferedReader);
            HttpResponse httpResponse = HttpResponse.ok();
            httpRequestHandler.handle(httpRequest, httpResponse);
            sendResponse(outputStream, httpResponse);
        } catch (IOException | RuntimeException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.error("Socket Close Error");
                e.printStackTrace();
            }
        }
    }

    private void sendResponse(OutputStream outputStream, HttpResponse httpResponse) throws IOException {
        sendResponseLine(outputStream, httpResponse);
        sendHeaders(outputStream, httpResponse);
        sendBody(outputStream, httpResponse);
    }

    private void sendResponseLine(OutputStream outputStream, HttpResponse httpResponse) throws IOException {
        outputStream.write(httpResponse.getResponseLine().getBytes());
        outputStream.write(CRLF.getBytes());
    }

    private void sendHeaders(OutputStream outputStream, HttpResponse httpResponse) throws IOException {
        outputStream.write(httpResponse.getHeaders().getBytes());
        outputStream.write(CRLF.getBytes());
    }

    private void sendBody(OutputStream outputStream, HttpResponse httpResponse) throws IOException {
        outputStream.write(httpResponse.getBody());
    }
}
