package exception;

import static util.HttpStatusCode.NOT_FOUND;

public class NotFoundException extends GeneralException {
    public NotFoundException() {
        super(NOT_FOUND.getStatusMessage(), NOT_FOUND.getStatusCode());
    }
}
