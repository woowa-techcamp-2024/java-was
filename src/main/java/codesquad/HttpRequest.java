package codesquad;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String uri;
    private String httpVersion;
    private Map<String, String> headers = new HashMap<>();

    public HttpRequest(String method, String uri, String httpVersion) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
    }

    @Override
    public String toString() {
        return "[ method: " + method + ", " +
                "uri: " + uri + ", " +
                "http: " + httpVersion + " ]";
    }

    public String getUri() {
        return this.uri;
    }

    public void setHeader(final String line) {
        String[] words = line.split(": ", 2);
        headers.put(words[0], words[1]);
    }
}
