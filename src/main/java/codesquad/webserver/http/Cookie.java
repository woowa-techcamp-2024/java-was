package codesquad.webserver.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Cookie {

    private static final String PATH = "Path";
    private static final String DOMAIN = "Domain";
    private static final String MAX_AGE = "Max-Age";
    private static final String SECURE = "Secure";
    private static final String HTTP_ONLY = "HttpOnly";

    private String name;
    private String value;

    private Map<String, String> attributes = new HashMap<>();

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public void expire() {
        value = "";
        setPath("/");
        setMaxAge(0);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public void setPath(String uri) {
        attributes.put(PATH, uri);
    }

    public void setHttpOnly(boolean httpOnly) {
        if (httpOnly) {
            attributes.put(HTTP_ONLY, "");
        }
    }

    public void setSecure(boolean secure) {
        if (secure) {
            attributes.put(SECURE, "");
        }
    }

    public void setMaxAge(int maxAge) {
        attributes.put(MAX_AGE, Integer.toString(maxAge));
    }

    public void setDomain(String domain) {
        attributes.put(DOMAIN, domain);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cookie cookie = (Cookie) o;
        return Objects.equals(name, cookie.name) && Objects.equals(value, cookie.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
