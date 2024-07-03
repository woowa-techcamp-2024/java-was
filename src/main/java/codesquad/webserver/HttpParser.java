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

        logger.debug("Request Line : {} {} {}", method, path, httpVersion);

        Map<String, String> headers = new HashMap<>();
        String headerLine;
        while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
            String[] headerParts = headerLine.split(":", 2);
            if (headerParts.length == 2) {
                headers.put(headerParts[0].trim(), headerParts[1].trim());
            }
        }

        logger.debug("Headers : {}", headers);

        StringBuilder body = new StringBuilder();
        if ("POST".equalsIgnoreCase(method) && headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            char[] buffer = new char[contentLength];
            int read = in.read(buffer, 0, contentLength);
            body.append(buffer, 0, read);
        }

        logger.debug("Body : {}", body);


        return new HttpRequest(method, path, httpVersion, headers, body.toString());
    }
}
