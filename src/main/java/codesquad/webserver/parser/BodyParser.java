package codesquad.webserver.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BodyParser {

    private static final Logger logger = LoggerFactory.getLogger(BodyParser.class);

    public static String parse(BufferedReader in, Map<String, String> headers) throws IOException {
        StringBuilder body = new StringBuilder();
        if (headers.containsKey("Content-Length")) {
            try {
                int contentLength = Integer.parseInt(headers.get("Content-Length"));
                char[] buffer = new char[contentLength];
                int read = in.read(buffer, 0, contentLength);
                body.append(buffer, 0, read);
                logger.debug("Body : {}", body);
            } catch (NumberFormatException e) {
                logger.error("Invalid Content-Length value: {}", headers.get("Content-Length"));
                throw new IOException("Invalid Content-Length value: " + headers.get("Content-Length"), e);
            }
        }
        return body.toString();
    }
}
