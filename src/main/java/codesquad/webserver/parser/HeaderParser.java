package codesquad.webserver.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HeaderParser {

    private static final Logger logger = LoggerFactory.getLogger(HeaderParser.class);

    public static Map<String, String> parse(BufferedReader in) throws IOException {
        String headerLine;
        Map<String, String> headers = new HashMap<>();
        while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
            String[] headerParts = headerLine.split(":", 2);
            if (headerParts.length == 2) {
                headers.put(headerParts[0].trim(), headerParts[1].trim());
            }
        }
        logger.debug("Headers : {}", headers);
        return headers;
    }
}
