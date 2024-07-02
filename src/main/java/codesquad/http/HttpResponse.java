package codesquad.http;

import codesquad.http.enums.StatusCode;
import codesquad.reader.FileByteReader;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private final String httpVersion;
    private final StatusCode statusCode;
    private final Map<String, String> headers;
    private byte[] body;

    private HttpResponse(String httpVersion, StatusCode statusCode, Map<String, String> headers,
        String requestUri) {
        log.debug("httpVersion: {}, statusCode: {}, headers: {}, requestUri: {}", httpVersion,
            statusCode,
            headers, requestUri);
        this.httpVersion = httpVersion;
        this.statusCode = statusCode;
        this.headers = headers;
        try {
            this.body = new FileByteReader(requestUri).readAllBytes();
        } catch (Exception e) {
            log.error(e.getMessage());
            this.body = new byte[0];
        }
    }

    public static HttpResponse of(String httpVersion, StatusCode statusCode,
        Map<String, String> headers, String requestUri) {
        return new HttpResponse(httpVersion, statusCode, headers, requestUri);
    }

    public byte[] generateResponse() {
        var tempResponse = (generateResponseLine() + generateHeaders() + "\r\n").getBytes();
        var response = new byte[tempResponse.length + body.length];
        System.arraycopy(tempResponse, 0, response, 0, tempResponse.length);
        System.arraycopy(body, 0, response, tempResponse.length, body.length);
        return response;
    }

    private String generateResponseLine() {
        return httpVersion + " " + statusCode.getCode() + " " + statusCode.name() + "\r\n";
    }

    private String generateHeaders() {
        StringBuilder sb = new StringBuilder();
        headers.forEach((key, value) -> sb.append(key).append(": ").append(value).append("\r\n"));
        return sb.toString();
    }

}
