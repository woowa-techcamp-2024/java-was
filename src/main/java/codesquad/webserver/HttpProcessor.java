package codesquad.webserver;

import static codesquad.utils.string.StringUtils.CRLF;

import codesquad.servlet.execption.ClientException;
import codesquad.servlet.filter.SessionAuthFilter;
import codesquad.servlet.handler.HttpRequestHandler;
import codesquad.utils.time.ZonedDateTimeGenerator;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.http.HttpStatus;
import codesquad.webserver.parser.HttpRequestMapper;
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

    private final HttpRequestMapper httpRequestMapper;
    private final HttpRequestHandler httpRequestHandler;
    private final SessionAuthFilter sessionAuthFilter;
    private final ZonedDateTimeGenerator zonedDateTimeGenerator;

    public HttpProcessor(HttpRequestMapper httpRequestMapper, HttpRequestHandler httpRequestHandler,
                         SessionAuthFilter sessionAuthFilter, ZonedDateTimeGenerator zonedDateTimeGenerator) {
        this.httpRequestMapper = httpRequestMapper;
        this.httpRequestHandler = httpRequestHandler;
        this.sessionAuthFilter = sessionAuthFilter;
        this.zonedDateTimeGenerator = zonedDateTimeGenerator;
    }

    public void process(Socket connection) {

        try (InputStream inputStream = connection.getInputStream();
             OutputStream outputStream = connection.getOutputStream()
        ) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            HttpRequest httpRequest;
            HttpResponse httpResponse = HttpResponse.ok();
            httpResponse.setDefaultHeaders(zonedDateTimeGenerator.now());

            try {
                httpRequest = httpRequestMapper.mapFrom(bufferedReader, httpResponse);
            } catch (ClientException e) {
                sendResponse(outputStream, httpResponse);
                return;
            }

            sessionAuthFilter.doFilter(httpRequest, httpResponse);
            if (httpResponse.getHttpStatus() == HttpStatus.OK) {
                httpRequestHandler.handle(httpRequest, httpResponse);
            }
            sendResponse(outputStream, httpResponse);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                logger.error("Socket Close Error");
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
