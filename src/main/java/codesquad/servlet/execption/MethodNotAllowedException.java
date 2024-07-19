package codesquad.servlet.execption;

import codesquad.webserver.http.HttpStatus;

public class MethodNotAllowedException extends ClientException {

    private HttpStatus httpStatus;


    public MethodNotAllowedException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
    
}
