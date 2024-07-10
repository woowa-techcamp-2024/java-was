package exception;

public class GeneralException {
    private final String message;
    private final int statusCode;

    public GeneralException(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
