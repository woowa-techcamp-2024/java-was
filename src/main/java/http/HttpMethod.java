package http;

public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE;

    public static HttpMethod of(String method) {
        if (method == null) {
            throw new IllegalArgumentException("method는 null일 수 없습니다.");
        }

        for (HttpMethod httpMethod : values()) {
            if (httpMethod.name().equalsIgnoreCase(method)) {
                return httpMethod;
            }
        }

        throw new IllegalArgumentException("유효하지 않은 HTTP 메소드입니다: " + method);
    }
}
