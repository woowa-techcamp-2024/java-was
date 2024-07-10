package codesquad.webserver.http;

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
}
