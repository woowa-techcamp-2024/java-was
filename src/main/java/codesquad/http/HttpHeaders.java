package codesquad.http;

import static codesquad.http.HttpHeaders.HeaderName.CONTENT_TYPE;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpHeaders {

    //TODO: Map<String, List<String>> 으로 변경 -> ex) Accept 헤더에 여러 value가 존재함
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

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : headers.keySet()) {
            sb.append(s).append(": ").append(headers.get(s)).append("\n");
        }
        return sb.toString();
    }

    public enum HeaderName {
        CONTENT_TYPE("Content-Type");

        private final String name;

        HeaderName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
