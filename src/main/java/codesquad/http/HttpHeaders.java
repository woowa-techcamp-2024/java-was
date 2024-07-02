package codesquad.http;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpHeaders {
    private final Logger log = LoggerFactory.getLogger(HttpHeaders.class);
    private final Map<String, String> headers = new HashMap<>();

    public void add(String line) {
        String[] row = line.split(":");
        headers.put(row[0], row[1].trim());
    }

    @Override
    public String toString() {
        return "HttpHeaders{" +
                "headers=" + headers +
                '}';
    }
}
