package codesquad.servlet.database.exception;

import codesquad.servlet.execption.ServerException;
import codesquad.webserver.http.HttpStatus;

public class InvalidDataAccessException extends ServerException {

    public InvalidDataAccessException(String s) {
        super(s, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
