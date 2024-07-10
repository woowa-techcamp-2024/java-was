package codesquad.http;

public record HttpCookie(String name, String value) {

    public String toCookieString() {
        return String.join("=", name(), value());
    }
}
