package codesquad.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class HttpHeaders {

    public static final String ACCEPT = "Accept";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String LOCATION = "Location";

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

    public void addValues(String name, Iterable<String> values) {
        values.forEach(value -> addValue(name, value));
    }

    public List<String> getValues(String name) {
        if (name == null) {
            return Collections.emptyList();
        }

        List<String> values = this.headers.getOrDefault(name, Collections.emptyList());
        return Collections.unmodifiableList(values);
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
