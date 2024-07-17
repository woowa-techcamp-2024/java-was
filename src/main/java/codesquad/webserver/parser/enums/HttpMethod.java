package codesquad.webserver.parser.enums;

import java.util.Arrays;

public enum HttpMethod {
    GET("get"), POST("post"), PUT("put"), DELETE("delete"), PATCH("patch");

    private final String description;

    HttpMethod(String description) {
        this.description = description;
    }

    public static HttpMethod find(String description) {
        return Arrays.stream(values())
                .filter(method -> method.description.equalsIgnoreCase(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(description + "은 지원하지 않습니다."));
    }

    @Override
    public String toString() {
        return description.toUpperCase();
    }
}
