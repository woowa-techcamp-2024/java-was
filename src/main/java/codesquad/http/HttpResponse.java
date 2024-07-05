package codesquad.http;

import static codesquad.utils.StringUtils.CRLF;

import java.util.Arrays;
import java.util.Map;

public class HttpResponse {

    private final HttpVersion httpVersion;
    private final HttpStatus httpStatus;
    private final HttpHeaders headers;
    private final byte[] body;

    public HttpResponse(HttpVersion httpVersion, HttpStatus httpStatus, HttpHeaders headers, byte[] body) {
        this.httpVersion = httpVersion;
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }

    public String getResponseLine() {
        String delimiter = " ";
        StringBuilder sb = new StringBuilder();
        sb.append(httpVersion.getName()).append(delimiter)
                .append(httpStatus.getCode()).append(delimiter)
                .append(httpStatus.getRepresentation());
        return sb.toString();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getHeaders() {
        String delimiter = ": ";
        Map<String, String> headers = this.headers.getHeaders();
        StringBuilder sb = new StringBuilder();
        for (String headerName : headers.keySet()) {
            sb.append(headerName).append(delimiter).append(headers.get(headerName)).append(CRLF);
        }
        return sb.toString();
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Status: ").append(httpStatus);
        sb.append("Headers: ").append(headers);
        sb.append("Response Body: ").append(Arrays.toString(body));
        return sb.toString();
    }
}
