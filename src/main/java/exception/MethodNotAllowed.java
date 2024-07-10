package exception;

public class MethodNotAllowed extends GeneralException{
    public MethodNotAllowed() {
        super("Method Not Allowed", 405);
    }
}
