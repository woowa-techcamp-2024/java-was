package codesquad.webserver.httpresponse;

import codesquad.webserver.session.cookie.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private int statusCode;
    private String statusMessage;
    private final List<Header> headers;
    private final List<HttpCookie> cookies;
    private byte[] body;

    public HttpResponse() {
        this.statusCode = 200;
        this.statusMessage = "OK";
        this.headers = new ArrayList<>();
        this.cookies = new ArrayList<>();
        this.body = new byte[0];
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void addHeader(String name, String value) {
        headers.add(new Header(name, value));
    }

    public void addCookie(HttpCookie cookie) {
        cookies.add(cookie);
    }

    public List<String> getHeader(String name) {
        return headers.stream()
                .filter(header -> header.getName().equalsIgnoreCase(name))
                .map(Header::getValue)
                .toList();
    }

    public String getHeaderValue(String name) {
        List<String> values = getHeader(name);
        return values != null && !values.isEmpty() ? values.get(0) : null;
    }

    public List<Header> getHeaders() {
        return new ArrayList<>(headers);
    }

    public List<HttpCookie> getCookies() {
        return new ArrayList<>(cookies);
    }

    public byte[] getBody() {
        return body.clone();
    }

    public void setBody(byte[] body) {
        this.body = body != null ? body.clone() : new byte[0];
    }

    public byte[] generateHttpResponse() {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusMessage).append("\r\n");

        for (Header header : headers) {
            responseBuilder.append(header.getName()).append(": ").append(header.getValue()).append("\r\n");
        }

        for (HttpCookie cookie : cookies) {
            responseBuilder.append("Set-Cookie: ").append(cookie.toSetCookieHeader()).append("\r\n");
        }

        responseBuilder.append("\r\n");

        byte[] headerBytes = responseBuilder.toString().getBytes();
        byte[] responseBytes = new byte[headerBytes.length + body.length];
        System.arraycopy(headerBytes, 0, responseBytes, 0, headerBytes.length);
        System.arraycopy(body, 0, responseBytes, headerBytes.length, body.length);

        return responseBytes;
    }

    @Override
    public String toString() {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusMessage).append("\r\n");

        for (Header header : headers) {
            responseBuilder.append(header.getName()).append(": ").append(header.getValue()).append("\r\n");
        }

        for (HttpCookie cookie : cookies) {
            responseBuilder.append("Set-Cookie: ").append(cookie.toSetCookieHeader()).append("\r\n");
        }

        responseBuilder.append("\r\n");

        return responseBuilder.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final HttpResponse response = new HttpResponse();

        public Builder statusCode(int statusCode) {
            response.setStatusCode(statusCode);
            return this;
        }

        public Builder statusMessage(String statusMessage) {
            response.setStatusMessage(statusMessage);
            return this;
        }

        public Builder header(String key, String value) {
            response.addHeader(key, value);
            return this;
        }

        public Builder cookie(HttpCookie cookie) {
            response.addCookie(cookie);
            return this;
        }

        public Builder body(byte[] body) {
            response.setBody(body);
            return this;
        }

        public HttpResponse build() {
            return response;
        }
    }

    public static class Header {
        private final String name;
        private final String value;

        public Header(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Header{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
