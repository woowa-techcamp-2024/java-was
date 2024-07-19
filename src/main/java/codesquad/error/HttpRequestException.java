package codesquad.error;

import codesquad.http.StatusCode;

public class HttpRequestException extends RuntimeException {

    private final StatusCode statusCode;

    public StatusCode statusCode() {
        return statusCode;
    }

    public HttpRequestException(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public HttpRequestException(StatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return String.join(" ", String.valueOf(statusCode().getValue()), statusCode().getPhrase());
    }
}
