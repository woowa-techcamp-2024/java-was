package codesquad.http;

import java.util.Optional;

public enum MediaType {

    TEXT_HTML("text/html", "html"),
    TEXT_CSS("text/css", "css"),
    TEXT_JAVASCRIPT("text/javascript", "js"),
    IMAGE_SVG("image/svg+xml", "svg"),
    IMAGE_PNG("image/png", "png"),
    IMAGE_JPEG("image/jpeg", "jpg"),
    IMAGE_ICON("image/vnd.microsoft.icon", "ico");

    private final String value;
    private final String fileExtension;

    MediaType(String value, String fileExtension) {
        this.value = value;
        this.fileExtension = fileExtension;
    }

    public String getValue() {
        return value;
    }

    public static Optional<MediaType> find(String extension) {
        for (MediaType mediaType : MediaType.values()) {
            if (mediaType.fileExtension.equals(extension)) {
                return Optional.of(mediaType);
            }
        }
        return Optional.empty();
    }

}
