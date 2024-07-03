package codesquad.http;

public enum HttpMediaType {

    TEXT_HTML("text/html");

    private final String name;

    HttpMediaType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
