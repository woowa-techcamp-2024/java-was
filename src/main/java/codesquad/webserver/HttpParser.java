package codesquad.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpParser {
    private static final Logger logger = LoggerFactory.getLogger(HttpParser.class);

    public HttpRequest parse(BufferedReader in) throws IOException {

        String requestLine = in.readLine();
        if (requestLine == null) {
            throw new IOException("Invalid request line");
        }

        String[] parts = requestLine.split(" ");
        if (parts.length != 3) {
            throw new IOException("Invalid request line: " + requestLine);
        }

        String method = parts[0];
        String path = parts[1];
        String httpVersion = parts[2];

        logger.debug(method + " " + path + " " + httpVersion);

        Map<String, String> headers = new HashMap<>();
        String headerLine;
        while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
            String[] headerParts = headerLine.split(":", 2);
            if (headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
            }
        }
        logger.debug(headerLine);

        return new HttpRequest(method, path, httpVersion, headers);
    }
}
