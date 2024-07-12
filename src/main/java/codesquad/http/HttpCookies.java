package codesquad.http;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpCookies {

    private final List<HttpCookie> cookies = new ArrayList<>();

    public void addCookie(String name, String value) {
        HttpCookie cookie = new HttpCookie(name, value);
        cookies.add(cookie);
    }

    public String toCookiesString() {
        return cookies.stream()
                .map(HttpCookie::toCookieString)
                .collect(Collectors.joining("; "));
    }

    public List<HttpCookie> getCookies() {
        return cookies;
    }
}
