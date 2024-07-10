package codesquad.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class HttpHeaders {

    public static final String ACCEPT = "Accept";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String LOCATION = "Location";
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String COOKIE = "Cookie";

    private final Map<String, List<String>> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public static HttpHeaders empty() {
        return new HttpHeaders();
    }

    public void putHeader(String[] nameAndValues) {
        String name = nameAndValues[0].trim();
        Arrays.stream(nameAndValues[1].split(",\\s*"))
                .forEach(value -> addValue(name, value));
    }

    public void addValue(String name, String value) {
        this.headers.computeIfAbsent(name, key -> new ArrayList<>())
                .add(value);
    }

    public List<String> getValues(String name) {
        if (name == null) {
            return Collections.emptyList();
        }

        List<String> values = this.headers.getOrDefault(name, Collections.emptyList());
        return Collections.unmodifiableList(values);
    }

    public Optional<String> getFirstValue(String name) {
        return getValues(name).stream()
                .findFirst();
    }

    public String getCookie(String cookieName) {
        return getFirstValue(COOKIE)
                .map(cookie -> findCookie(cookieName, cookie))
                .orElse("");
    }

    private String findCookie(String cookieName, String cookies) {
        return Arrays.stream(cookies.split(";"))
                .map(String::trim)
                .filter(cookie -> cookie.startsWith(cookieName + "="))
                .map(cookiePair -> cookiePair.substring(cookieName.length() + 1))
                .findFirst()
                .orElse("");
    }

    public String toText() {
        return headers.entrySet()
                .stream()
                .flatMap(entry -> entry.getValue()
                        .stream()
                        .map(value -> entry.getKey() + ": " + value + "\r\n"))
                .collect(Collectors.joining());
    }

    @Override
    public String toString() {
        return "HttpHeaders{" +
                "headers=" + headers +
                '}';
    }
}
