package codesquad.webserver.exception;

import java.io.IOException;

public class FileReadException extends RuntimeException {
    public FileReadException(String message){
        super(message);
    }
    public FileReadException(Exception e){
        super(e);
    }
}
