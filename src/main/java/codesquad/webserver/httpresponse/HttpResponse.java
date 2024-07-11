package codesquad.webserver.httpresponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private final int statusCode;
    private final String statusMessage;
    private final Map<String, List<String>> headers;
    private final byte[] body;

    private HttpResponse(int statusCode, String statusMessage, Map<String, List<String>> headers, byte[] body) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.headers = new ConcurrentHashMap<>();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            this.headers.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        this.body = body != null ? body.clone() : new byte[0];
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public Map<String, List<String>> getHeaders() {
        Map<String, List<String>> copiedHeaders = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            copiedHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copiedHeaders;
    }

    public byte[] getBody() {
        return body.clone();
    }

    public byte[] generateHttpResponse() {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusMessage).append("\r\n");

        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            List<String> headerValues = entry.getValue();

            String combinedValue = String.join("; ", headerValues);
            responseBuilder.append(headerName).append(": ").append(combinedValue).append("\r\n");
        }

        responseBuilder.append("\r\n");

        byte[] headerBytes = responseBuilder.toString().getBytes();
        byte[] responseBytes = new byte[headerBytes.length + body.length];
        System.arraycopy(headerBytes, 0, responseBytes, 0, headerBytes.length);
        System.arraycopy(body, 0, responseBytes, headerBytes.length, body.length);

        return responseBytes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int statusCode = 200;
        private String statusMessage = "OK";
        private final Map<String, List<String>> headers = new HashMap<>();
        private byte[] body;

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder statusMessage(String statusMessage) {
            this.statusMessage = statusMessage;
            return this;
        }

        public Builder header(String key, String value) {
            headers.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
            return this;
        }

        public Builder header(String key, List<String> values) {
            headers.put(key, new ArrayList<>(values));
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(statusCode, statusMessage, headers, body);
        }
    }
}