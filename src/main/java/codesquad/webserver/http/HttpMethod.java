package codesquad.webserver.http;

import static codesquad.webserver.http.HttpStatus.METHOD_NOT_ALLOWED;

import codesquad.servlet.execption.MethodNotAllowedException;
import java.util.Arrays;

public enum HttpMethod {

    GET("GET"),
    POST("POST");

    private final String name;

    HttpMethod(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "HttpMethod='" + name + '\'';
    }

    public static HttpMethod fromString(String name) {
        return Arrays.stream(values())
                .filter(httpMethod -> httpMethod.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new MethodNotAllowedException("지원하지 않는 HTTP 메서드입니다.", METHOD_NOT_ALLOWED));
    }
}
