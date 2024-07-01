package codesquad.webserver;

import java.util.Map;

public record HttpRequest(String method, String path, String httpVersion, Map<String, String> headers) {

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                ", headers=" + headers +
                '}';
    }
}
