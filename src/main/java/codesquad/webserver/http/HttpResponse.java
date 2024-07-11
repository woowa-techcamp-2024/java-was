package codesquad.webserver.http;

import static codesquad.utils.string.StringUtils.CRLF;
import static codesquad.webserver.http.HttpHeaders.HeaderName.CONNECTION;
import static codesquad.webserver.http.HttpHeaders.HeaderName.DATE;
import static codesquad.webserver.http.HttpHeaders.HeaderName.LOCATION;
import static codesquad.webserver.http.HttpHeaders.HeaderName.SERVER;
import static codesquad.webserver.http.HttpHeaders.HeaderName.SET_COOKIE;
import static codesquad.webserver.http.HttpStatus.NOT_FOUND;
import static codesquad.webserver.http.HttpStatus.UNAUTHORIZED;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpResponse {

    private HttpResponseLine httpResponseLine;
    private HttpHeaders headers;
    private byte[] body;
    private List<Cookie> cookies = new ArrayList<>();

    public HttpResponse(HttpResponseLine httpResponseLine, HttpHeaders headers, byte[] body) {
        this.httpResponseLine = httpResponseLine;
        this.headers = headers;
        this.body = body;
    }

    /*---------- static factory method ----------*/

    public static HttpResponse ok() {
        HttpResponseLine httpResponseLine = new HttpResponseLine(HttpVersion.HTTP1_1, HttpStatus.OK);
        HttpHeaders empty = HttpHeaders.empty();
        byte[] bytes = new byte[0];
        return new HttpResponse(httpResponseLine, empty, bytes);
    }


    /*---------- setter http response line ----------*/

    public void notFound() {
        HttpResponseLine httpResponseLine = new HttpResponseLine(HttpVersion.HTTP1_1, HttpStatus.NOT_FOUND);
        byte[] notFound = NOT_FOUND.getRepresentation().getBytes();
        setHttpResponseLine(httpResponseLine);
        setBody(notFound);
    }

    public void unauthorized() {
        HttpResponseLine httpResponseLine = new HttpResponseLine(HttpVersion.HTTP1_1, HttpStatus.UNAUTHORIZED);
        byte[] unauthorized = UNAUTHORIZED.getRepresentation().getBytes();
        setHttpResponseLine(httpResponseLine);
        setBody(unauthorized);
    }

    public void setHttpResponseLine(HttpResponseLine httpResponseLine) {
        this.httpResponseLine = httpResponseLine;
    }

    /*---------- setter headers ----------*/

    public void setDefaultHeaders(ZonedDateTime zonedDateTime) {
        setDate(zonedDateTime);
        setServer();
        setConnectionClose();
    }

    private void setDate(ZonedDateTime zonedDateTime) {
        this.headers.setHeader(DATE.getName(), zonedDateTime.toString());
    }

    private void setServer() {
        this.headers.setHeader(SERVER.getName(), "Woowah WAS Server/1.0");
    }

    private void setConnectionClose() {
        this.headers.setHeader(CONNECTION.getName(), "close");
    }

    public void setBody(byte[] body) {
        this.body = body;
        setContentLength(body.length);
    }

    private void setContentLength(int contentLength) {
        this.headers.setContentLength(contentLength);
    }

    public void setContentType(HttpMediaType httpMediaType) {
        this.headers.setContentType(httpMediaType);
    }

    public void addCookie(Cookie cookie) {
        if (cookies.contains(cookie)) {
            return;
        }
        cookies.add(cookie);
        setCookie(cookie);
    }

    private void setCookie(Cookie cookie) {
        StringBuilder sb = new StringBuilder();

        final String cookieName = cookie.getName();
        final String cookieValue = cookie.getValue();
        final Map<String, String> cookieAttributes = cookie.getAttributes();

        final String cookieNameAndValue = cookieName + "=" + cookieValue;
        sb.append(cookieNameAndValue);

        for (String attributeName : cookieAttributes.keySet()) {
            String attributeValue = cookieAttributes.get(attributeName);
            sb.append("; ").append(attributeName);

            if (attributeValue.isEmpty()) {
                continue;
            }
            sb.append("=").append(attributeValue);
        }
        headers.setHeader(SET_COOKIE.getName(), sb.toString());
    }

    public void sendRedirect(String redirectUrl) {
        httpResponseLine.setStatus(HttpStatus.FOUND);
        headers.setHeader(LOCATION.getName(), redirectUrl);
    }

    /*---------- getter ----------*/

    public Optional<String> getRedirect() {
        return headers.getHeaderValue(LOCATION.getName());
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

    public List<Cookie> getCookies() {
        return Collections.unmodifiableList(cookies);
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
