package codesquad.webserver.http;

import java.util.Map;

public class HttpBody {
    private final Map<String, Object> body;

    public HttpBody(Map<String, Object> jsonMap) {
        this.body = jsonMap;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "body= " + body.toString();
    }
}
