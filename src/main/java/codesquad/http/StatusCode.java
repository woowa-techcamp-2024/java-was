package codesquad.http;

public enum StatusCode {

    OK(200, "OK"),
    NOT_FOUND(404, "Not Found");

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
