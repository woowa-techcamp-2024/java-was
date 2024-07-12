package codesquad.webserver.dispatcher.view;

public enum ViewName {
    TEMPLATE_VIEW("templateView"),
    EXCEPTION_VIEW("exceptionView"),
    REDIRECT_VIEW("redirectView"),
    STATIC_FILE_VIEW("staticFileView"),
    JSON_VIEW("jsonView");

    private final String value;

    ViewName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
