package codesquad.webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestParser {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestParser.class);

    public HttpRequest parse(final BufferedReader bufferedReader) throws IOException {
        String requestLine;

        requestLine = bufferedReader.readLine();
        HttpRequestLine firstLine = parseHttpRequestFirstLine(requestLine);
        HttpHeaders headers = parseHeaders(bufferedReader);

        return new HttpRequest(firstLine, headers);
    }

    private HttpRequestLine parseHttpRequestFirstLine(String requestLine) throws UnsupportedEncodingException {
        logger.debug("requestLine: {}", requestLine);
        String[] startLineParts = requestLine.split(" ");

        if (startLineParts.length != 3) {
            throw new IllegalArgumentException("Invalid request line: " + requestLine);
        }

        HttpMethod method = HttpMethod.valueOf(startLineParts[0]);
        String defaultPath = startLineParts[1];
        HttpVersion version = HttpVersion.of(startLineParts[2]);

        String[] pathParts = defaultPath.split("\\?");
        if (pathParts.length == 1) {
            HttpPath httpPath = HttpPath.ofOnlyDefaultPath(pathParts[0]);
            HttpRequestLine httpRequestLine = new HttpRequestLine(method, httpPath, version);
            logger.debug("httpRequestFirstLine: {}", httpRequestLine);
            return httpRequestLine;
        }

        defaultPath = pathParts[0];
        String queryStrings = pathParts[1];
        logger.debug("queryStrings: {}", queryStrings);

        String[] queryStringParts = queryStrings.split("&");
        Map<String, String> allQueryStrings = new HashMap<>();
        for (String queryStringPart : queryStringParts) {
            String decodedQueryString = URLDecoder.decode(queryStringPart, "UTF-8");
            String[] queryStringComponentPart = decodedQueryString.split("=");
            allQueryStrings.put(queryStringComponentPart[0], queryStringComponentPart[1]);
        }

        HttpPath httpPath = HttpPath.of(defaultPath, allQueryStrings);
        HttpRequestLine httpRequestLine = new HttpRequestLine(method, httpPath, version);
        logger.debug("httpRequestFirstLine: {}", httpRequestLine);
        return httpRequestLine;
    }

    private HttpHeaders parseHeaders(BufferedReader bufferedReader) throws IOException {
        String requestLine;
        Map<String, String> headers = new HashMap<>();
        while (!(requestLine = bufferedReader.readLine()).isBlank()) {
            String[] headerParts = requestLine.split(": ", 2);
            headers.put(headerParts[0], headerParts[1]);
        }
        return HttpHeaders.of(headers);
    }

}
