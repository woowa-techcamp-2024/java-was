package codesquad;

import codesquad.handler.HandlersMapper;
import codesquad.handler.RequestHandler;
import codesquad.http.HttpRequest;
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
    private final HandlersMapper handlersMapper;

    public HttpRequestProcessor(Socket socket, HandlersMapper handlersMapper) {
        this.socket = socket;
        this.handlersMapper = handlersMapper;
        log.debug("Client connected");
    }

    @Override
    public void run() {
        try {
            HttpRequest httpRequest = requestParser.parse(readHttpRequest());
            log.debug("httpRequest = {}", httpRequest);

            RequestHandler requestHandler = handlersMapper.getRequestHandler(httpRequest);
            HttpResponse httpResponse = requestHandler.handle(httpRequest);
            try (OutputStream clientOutput = socket.getOutputStream()) {
                clientOutput.write(httpResponse.toBytes());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String readHttpRequest() throws IOException {
        InputStream inputStream = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder request = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            request.append(line)
                    .append("\r\n");
        }
        return request.toString();
    }

}
