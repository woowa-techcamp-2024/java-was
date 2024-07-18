package codesquad.http.element;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestBody {
    private final String body;
    private Map<String, String> bodyMap = new HashMap<>();

    public RequestBody(String body) {
        if (body == null) {
            this.body = "";
            return;
        }

        String[] parts = body.split("&");
        for (String part: parts) {
            String[] keyValue = part.split("=");
            bodyMap.put(keyValue[0], URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8));
        }
        this.body = body;
    }

    public RequestBody(String body, Map<String, String> map) {
        this.body = body;
        this.bodyMap = map;
    }

    public Map<String, String> getBodyMap() {
        return bodyMap;
    }

    @Override
    public String toString() {
        if (body == null) return "";
        return body;
    }
}
