package codesquad.webserver.http;

public enum HttpMediaType {

    TEXT_HTML("text/html"),
    TEXT_CSS("text/css"),
    APPLICATION_JSON("application/json"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    IMAGE_X_ICON("image/x-icon"),
    IMAGE_PNG("image/png"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_SVG("image/svg+xml");

    private final String name;

    HttpMediaType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
