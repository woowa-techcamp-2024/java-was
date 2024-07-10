package codesquad.webserver.exception;

public class BeanCreationException extends RuntimeException {
    public BeanCreationException() {
        super();
    }

    public BeanCreationException(String message) {
        super(message);
    }

    public BeanCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanCreationException(Throwable cause) {
        super(cause);
    }
}
