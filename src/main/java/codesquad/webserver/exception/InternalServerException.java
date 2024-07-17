package codesquad.webserver.exception;

public class InternalServerException extends RuntimeException{
    public InternalServerException(String message){
        super(message);
    }
    public InternalServerException(Exception e){
        super(e);
    }
}
