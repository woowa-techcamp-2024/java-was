package codesquad.http;

public enum HttpStatus {

    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request");

    private final int code;
    private final String representation;

    HttpStatus(int code, String representation) {
        this.code = code;
        this.representation = representation;
    }

    public int getCode() {
        return code;
    }

    public String getRepresentation() {
        return representation;
    }
}
