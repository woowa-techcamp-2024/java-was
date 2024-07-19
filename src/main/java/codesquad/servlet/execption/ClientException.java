package codesquad.servlet.execption;

import codesquad.webserver.http.HttpStatus;

public class ClientException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode errorCode;

    public ClientException(Throwable cause, HttpStatus httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public ClientException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ClientException(String message, HttpStatus httpStatus, ErrorCode errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }


    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
