package codesquad;

import codesquad.error.HttpRequestException;
import codesquad.handler.HandlersMapper;
import codesquad.handler.RequestHandler;
import codesquad.http.HttpHeaders;
import codesquad.http.HttpRequest;
import codesquad.http.HttpRequestReader;
import codesquad.http.HttpResponse;
import codesquad.http.MediaType;
import codesquad.http.ResponseWriter;
import codesquad.http.StatusCode;
import codesquad.http.parser.HttpRequestParser;
import codesquad.http.parser.MultipartParser;
import codesquad.http.parser.ParsersFactory;
import codesquad.http.session.SessionContextFactory;
import codesquad.http.session.SessionContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

public class HttpRequestProcessor implements Runnable {

    private final Logger log = LoggerFactory.getLogger(HttpRequestProcessor.class);

    private final Socket socket;
    private final HttpRequestParser requestParser = ParsersFactory.getHttpRequestParser();
    private final HttpRequestReader requestReader = HttpRequestReader.getInstance();
    private final HandlersMapper handlersMapper;
    private final ResponseWriter responseWriter;
    private final SessionContextFactory sessionContextFactory = SessionContextFactory.getInstance();

    public HttpRequestProcessor(Socket socket, HandlersMapper handlersMapper) throws IOException {
        this.socket = socket;
        this.handlersMapper = handlersMapper;
        this.responseWriter = new ResponseWriter(socket.getOutputStream());
        log.debug("Client connected");
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            String requestHeader = requestReader.readHeader(bufferedInputStream);
            HttpRequest httpRequest = requestParser.parse(requestHeader);
            if (httpRequest == null) {
                return;
            }

            if (!httpRequest.method().equals("GET")) {
                extractRequestBody(httpRequest, bufferedInputStream);
            }

            sessionContextFactory.createSessionContext(httpRequest);
            RequestHandler requestHandler = handlersMapper.getRequestHandler(httpRequest);
            HttpResponse httpResponse = requestHandler.handle(httpRequest);
            responseWriter.write(httpResponse);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            SessionContextHolder.clear();
        }
    }

    private void extractRequestBody(HttpRequest httpRequest, InputStream inputStream) throws IOException {
        String headerValue = httpRequest.firstHeaderValue(HttpHeaders.CONTENT_LENGTH)
                .orElseThrow(() -> new HttpRequestException(StatusCode.BAD_REQUEST, "[ERROR] Content-Length가 누락되었습니다."));
        int contentLength = Integer.parseInt(headerValue);
        String contentType = httpRequest.firstHeaderValue(HttpHeaders.CONTENT_TYPE)
                .orElseThrow(() -> new HttpRequestException(StatusCode.BAD_REQUEST, "[ERROR] Content-Type이 누락되었습니다."));
        byte[] requestBody = requestReader.readBody(inputStream, contentLength);
        if (contentType.startsWith(MediaType.MULTIPART_FORM_DATA.getValue())) {
            Map<String, Object> parts = MultipartParser.parse(requestBody, contentType);
            httpRequest.setMultipart(parts);
        } else {
            httpRequest.setBody(new String(requestBody));
        }
    }
}
