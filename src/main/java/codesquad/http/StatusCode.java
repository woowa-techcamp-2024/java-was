package codesquad.http;

public enum StatusCode {

    OK(200, "OK"),

    SEE_OTHER(303, "See Other"),

    NOT_FOUND(404, "Not Found"),
    BAD_REQUEST(400, "Bad Request"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int value;
    private final String phrase;

    StatusCode(int value, String phrase) {
        this.value = value;
        this.phrase = phrase;
    }

    public int getValue() {
        return value;
    }

    public String getPhrase() {
        return phrase;
    }

}
