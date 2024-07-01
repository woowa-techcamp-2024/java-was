package codesquad.http.enums;

public enum StatusCode {
    OK(200), CREATED(201), NO_CONTENT(204), MOVED_PERMANENTLY(301), FOUND(302), BAD_REQUEST(
        400), UNAUTHORIZED(401), FORBIDDEN(403), NOT_FOUND(404), METHOD_NOT_ALLOWED(
        405), INTERNAL_SERVER_ERROR(500), NOT_IMPLEMENTED(501), BAD_GATEWAY(
        502), SERVICE_UNAVAILABLE(503), HTTP_VERSION_NOT_SUPPORTED(505);

    private final int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
