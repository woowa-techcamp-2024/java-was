package exception;

import static util.HttpStatusCode.INTERNAL_SERVER_ERROR;

public class InternalServerError extends GeneralException {

    public InternalServerError() {
        super(INTERNAL_SERVER_ERROR.getStatusMessage(), INTERNAL_SERVER_ERROR.getStatusCode());
    }

    public InternalServerError(String message) {
        super(message, INTERNAL_SERVER_ERROR.getStatusCode());
    }
}
