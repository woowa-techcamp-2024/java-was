package codesquad.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private String httpMethod;
    private String requestUri;
    private String httpVersion;
    private Map<String, String> headers;

    private HttpRequest(InputStream inputStream) {
        try {
            var br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            parseRequestLine(br.readLine());
            headers = parseHeaders(br);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static HttpRequest from(InputStream inputStream) {
        return new HttpRequest(inputStream);
    }

    private void parseRequestLine(String requestLine) {
        if (requestLine == null || requestLine.isBlank()) {
            log.error("요청 라인이 없습니다.");
            throw new IllegalArgumentException("요청 라인이 없습니다.");
        }
        log.debug(requestLine);
        var tokens = requestLine.split(" ");
        if (tokens.length != 3) {
            log.error("요청 라인이 올바르지 않습니다.");
            throw new IllegalArgumentException("요청 라인이 올바르지 않습니다.");
        }
        httpMethod = tokens[0];
        requestUri = tokens[1];
        httpVersion = tokens[2];
    }

    private Map<String, String> parseHeaders(BufferedReader br) {
        var headerMap = new HashMap<String, String>();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) {
                    break;
                }
                log.debug(line);
                var header = line.split(": ");
                headerMap.put(header[0], header[1]);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return headerMap;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
