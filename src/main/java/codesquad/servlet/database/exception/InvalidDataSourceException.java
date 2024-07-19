package codesquad.servlet.database.exception;

import codesquad.servlet.execption.ServerException;
import codesquad.webserver.http.HttpStatus;

public class InvalidDataSourceException extends ServerException {

    public InvalidDataSourceException(Throwable cause) {
        super(cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
