package http;

public class Cookie {
    private final String name;
    private final String value;
    private final Integer expires;
    private final Integer maxAge;
    private final String path;
    private final Boolean httpOnly;

    public Cookie(String name, String value, Integer expires, Integer maxAge, String path, Boolean httpOnly) {
        this.name = name;
        this.value = value;
        this.expires = expires;
        this.maxAge = maxAge;
        this.path = path;
        this.httpOnly = httpOnly;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder cookieStringBuilder = new StringBuilder("Set-Cookie: ");
        cookieStringBuilder.append(name).append("=").append(value);
        if (expires != null) {
            cookieStringBuilder.append("; Expires=").append(expires);
        }
        if (maxAge != null) {
            cookieStringBuilder.append("; Max-Age=").append(maxAge);
        }
        if (path != null && !path.isEmpty()) {
            cookieStringBuilder.append("; Path=").append(path);
        }
        if (httpOnly != null && httpOnly) {
            cookieStringBuilder.append("; HttpOnly");
        }
        cookieStringBuilder.append(";");
        return cookieStringBuilder.toString();
    }
}
