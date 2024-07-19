package codesquad.servlet.execption;

public class CookieNotFoundException extends IllegalArgumentException {

    public CookieNotFoundException(String s) {
        super(s);
    }
    
}
