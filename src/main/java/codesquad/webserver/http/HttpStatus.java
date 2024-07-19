package codesquad.webserver.http;

public enum HttpStatus {

    OK(200, "OK"),
    FOUND(302, "Found"),
    SEE_OTHER(303, "See Other"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

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
