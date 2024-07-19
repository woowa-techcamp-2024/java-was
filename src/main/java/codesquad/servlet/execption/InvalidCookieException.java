package codesquad.servlet.execption;

public class InvalidCookieException extends IllegalArgumentException {

    public InvalidCookieException(String s) {
        super(s);
    }
    
}
