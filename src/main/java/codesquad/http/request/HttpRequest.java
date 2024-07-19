package codesquad.http.request;

import java.util.Map;
import java.util.Optional;

public record HttpRequest(String method, String path, String version, Map<String, String> headers, byte[] body) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Method: ").append(method).append("\n");
        sb.append("Path: ").append(path).append("\n");
        sb.append("Version: ").append(version).append("\n");
        headers.forEach((key, value) -> sb.append(key).append(": ").append(value).append("\n"));
        sb.append("Body: ").append(new String(body)).append("\n");
        return sb.toString();
    }

    public Optional<String> getCookie() {
        return Optional.ofNullable(headers.get("Cookie"));
    }

}
