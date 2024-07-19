package codesquad.webserver.http;

import java.util.Arrays;

public enum HttpMediaType {

    TEXT_HTML("text/html"),
    TEXT_CSS("text/css"),
    APPLICATION_JSON("application/json"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    IMAGE_X_ICON("image/x-icon"),
    IMAGE_PNG("image/png"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_GIF("image/gif"),
    IMAGE_SVG("image/svg+xml");

    private final String name;

    HttpMediaType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static HttpMediaType fromName(String name) {
        return Arrays.stream(values())
                .filter(httpMediaType -> httpMediaType.name.equals(name))
                .findFirst()
                .orElseThrow();
    }
}
