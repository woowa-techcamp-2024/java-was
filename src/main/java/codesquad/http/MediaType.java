package codesquad.http;

import codesquad.error.HttpRequestException;

import java.util.Arrays;

public enum MediaType {

    TEXT_HTML("text/html; charset=utf-8", "html"),
    TEXT_CSS("text/css", "css"),
    TEXT_JAVASCRIPT("text/javascript", "js"),
    TEXT_PLAIN("text/plain; charset=utf-8", "txt"),

    IMAGE_SVG("image/svg+xml", "svg"),
    IMAGE_PNG("image/png", "png"),
    IMAGE_JPEG("image/jpeg", "jpg,jpeg"),
    IMAGE_GIF("image/gif", "gif"),
    IMAGE_ICON("image/vnd.microsoft.icon", "ico"),

    APPLICATION_JSON("application/json", "json"),
    APPLICATION_XML("application/xml", "xml"),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded", "form"),

    MULTIPART_FORM_DATA("multipart/form-data", "");

    private final String value;
    private final String fileExtension;

    MediaType(String value, String fileExtension) {
        this.value = value;
        this.fileExtension = fileExtension;
    }

    public String getValue() {
        return value;
    }

    public static MediaType find(String extension) {
        return Arrays.stream(values())
                .filter(type -> type.fileExtension.contains(extension))
                .findFirst()
                .orElseThrow(() -> new HttpRequestException(StatusCode.BAD_REQUEST, "[ERROR] 지원하지 않는 파일 형식입니다."));
    }
}
