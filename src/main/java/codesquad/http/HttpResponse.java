package codesquad.http;

import java.util.Map;

public class HttpResponse {

    private final HttpVersion httpVersion;
    private final HttpStatus httpStatus;
    private final HttpHeaders headers;
    private final String body;

    public HttpResponse(HttpVersion httpVersion, HttpStatus httpStatus, HttpHeaders headers, String body) {
        this.httpVersion = httpVersion;
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Map<String, String> getHeaders() {
        return headers.getHeaders();
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Status: ").append(httpStatus);
        sb.append("Headers: ").append(headers);
        sb.append("Response Body: ").append(body);
        return sb.toString();
    }
}
