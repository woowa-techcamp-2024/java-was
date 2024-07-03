package codesquad.http;

public enum HttpMethod {

    GET("GET");

    private final String name;

    HttpMethod(String name) {
        this.name = name;
    }

}
