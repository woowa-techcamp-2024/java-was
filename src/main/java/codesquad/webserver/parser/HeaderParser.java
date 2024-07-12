package codesquad.webserver.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class HeaderParser {

    public static Map<String, List<String>> parse(BufferedReader in) throws IOException {
        String headerLine;
        Map<String, List<String>> headers = new HashMap<>();
        while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
            String[] headerParts = headerLine.split(":", 2);
            if (headerParts.length == 2) {
                String headerName = headerParts[0].trim();
                String headerValue = headerParts[1].trim();
                headers.computeIfAbsent(headerName, k -> new ArrayList<>()).add(headerValue);
            }
        }
        return headers;
    }
}
