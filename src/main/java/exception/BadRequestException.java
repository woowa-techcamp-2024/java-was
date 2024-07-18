package exception;

import static util.HttpStatusCode.BAD_REQUEST;

public class BadRequestException extends GeneralException {
    public BadRequestException(String message) {
        super(message, BAD_REQUEST.getStatusCode());
    }
}
