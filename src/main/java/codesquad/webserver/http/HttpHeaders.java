package codesquad.webserver.http;

import static codesquad.webserver.http.HttpHeaders.HeaderName.CONTENT_LENGTH;
import static codesquad.webserver.http.HttpHeaders.HeaderName.CONTENT_TYPE;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpHeaders {

    private final Map<String, String> headers;

    private HttpHeaders(final Map<String, String> headers) {
        this.headers = headers;
    }

    public static HttpHeaders of(final Map<String, String> headers) {
        return new HttpHeaders(headers);
    }

    public static HttpHeaders empty() {
        return new HttpHeaders(new HashMap<>());
    }

    public void setContentType(final HttpMediaType mediaType) {
        headers.put(CONTENT_TYPE.getName(), mediaType.getName());
    }

    public void setContentLength(final long contentLength) {
        headers.put(CONTENT_LENGTH.getName(), String.valueOf(contentLength));
    }

    public void setHeader(final String name, final String value) {
        headers.put(name, value);
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public String getHeader(final String name) {
        return headers.get(name);
    }

    public Optional<String> getHeaderValue(final String name) {
        return Optional.ofNullable(headers.get(name));
    }

    public int size() {
        return headers.size();
    }

    public int getContentLength() {
        if (headers.containsKey(CONTENT_LENGTH.getName())) {
            return Integer.parseInt(headers.get(CONTENT_LENGTH.getName()));
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : headers.keySet()) {
            sb.append(s).append(": ").append(headers.get(s)).append("\n");
        }
        return sb.toString();
    }

    public enum HeaderName {
        DATE("Date"),
        SERVER("Server"),
        CONNECTION("Connection"),
        CONTENT_TYPE("Content-Type"),
        CONTENT_LENGTH("Content-Length"),
        LOCATION("Location"),
        SET_COOKIE("Set-Cookie");

        private final String name;

        HeaderName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
