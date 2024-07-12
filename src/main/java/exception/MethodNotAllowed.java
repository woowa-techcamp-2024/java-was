package exception;

import static util.HttpStatusCode.METHOD_NOT_ALLOWED;

public class MethodNotAllowed extends GeneralException{
    public MethodNotAllowed() {
        super(METHOD_NOT_ALLOWED.getStatusMessage(), METHOD_NOT_ALLOWED.getStatusCode());
    }
}
