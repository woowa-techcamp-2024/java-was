package codesquad.http;

import java.util.Map;

public record HttpRequest(String method, String uri, String httpVersion, Map<String, String> headers) {

    @Override
    public String toString() {
        return "[ method: " + method + ", " +
                "uri: " + uri + ", " +
                "http: " + httpVersion + " ]";
    }

    public void setHeader(final String key, final String value) {
        headers.put(key, value);
    }
}
