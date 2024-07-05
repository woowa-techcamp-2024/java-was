package codesquad.webserver.http;

import static codesquad.utils.StringUtils.CRLF;

import codesquad.webserver.http.HttpHeaders.HeaderName;
import java.util.Arrays;
import java.util.Map;

public class HttpResponse {

    private HttpResponseLine httpResponseLine;
    private HttpHeaders headers;
    private byte[] body;

    public HttpResponse(HttpResponseLine httpResponseLine, HttpHeaders headers, byte[] body) {
        this.httpResponseLine = httpResponseLine;
        this.headers = headers;
        this.body = body;
    }

    public static HttpResponse ok() {
        HttpResponseLine httpResponseLine = new HttpResponseLine(HttpVersion.HTTP1_1, HttpStatus.OK);
        HttpHeaders empty = HttpHeaders.empty();
        byte[] bytes = new byte[0];
        return new HttpResponse(httpResponseLine, empty, bytes);
    }

    public void setHttpResponseLine(HttpResponseLine httpResponseLine) {
        this.httpResponseLine = httpResponseLine;
    }

    public void setContentType(HttpMediaType httpMediaType) {
        this.headers.setContentType(httpMediaType);
    }

    public void setContentLength(int contentLength) {
        this.headers.setContentLength(contentLength);
    }

    public void setBadRequest() {
        this.httpResponseLine = new HttpResponseLine(HttpVersion.HTTP1_1, HttpStatus.BAD_REQUEST);
        this.headers = HttpHeaders.empty();
        this.body = new byte[0];
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void sendRedirect(String redirectUrl) {
        httpResponseLine.setStatus(HttpStatus.SEE_OTHER);
        headers.setHeader(HeaderName.LOCATION.getName(), redirectUrl);
    }

    public String getResponseLine() {
        String delimiter = " ";
        StringBuilder sb = new StringBuilder();
        HttpVersion version = httpResponseLine.getVersion();
        HttpStatus status = httpResponseLine.getStatus();

        sb.append(version.getName()).append(delimiter)
                .append(status.getCode()).append(delimiter)
                .append(status.getRepresentation());
        return sb.toString();
    }

    public HttpStatus getHttpStatus() {
        return httpResponseLine.getStatus();
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
        sb.append("Status: ").append(httpResponseLine.getStatus());
        sb.append("Headers: ").append(headers);
        sb.append("Response Body: ").append(Arrays.toString(body));
        return sb.toString();
    }
}
