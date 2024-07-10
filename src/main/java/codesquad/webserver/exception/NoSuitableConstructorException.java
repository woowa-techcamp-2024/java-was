package codesquad.webserver.exception;

public class NoSuitableConstructorException extends RuntimeException {
    public NoSuitableConstructorException() {
        super();
    }

    public NoSuitableConstructorException(String message) {
        super(message);
    }

    public NoSuitableConstructorException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuitableConstructorException(Throwable cause) {
        super(cause);
    }
}
