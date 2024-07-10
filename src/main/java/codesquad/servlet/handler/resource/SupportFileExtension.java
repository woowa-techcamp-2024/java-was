package codesquad.servlet.handler.resource;

public enum SupportFileExtension {

    HTML("html"),
    CSS("css"),
    JSON("json"),
    ICO("ico"),
    PNG("png"),
    JPEG("jpeg"),
    SVG("svg");

    private final String name;

    SupportFileExtension(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
