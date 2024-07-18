package codesquad.webserver.parser;

import codesquad.webserver.httprequest.HttpRequest;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HeaderParser {

    private static final Logger logger = LoggerFactory.getLogger(HeaderParser.class);

    public static HttpRequest parse(BufferedInputStream in, HttpRequest request) throws IOException {
        Map<String, List<String>> headers = new HashMap<>();
        ByteArrayOutputStream lineBuffer = new ByteArrayOutputStream();

        int previousByte = -1;
        int currentByte;
        boolean isEndOfHeaders = false;

        while ((currentByte = in.read()) != -1) {
            if (currentByte == '\n' && previousByte == '\r') {
                if (lineBuffer.size() <= 2) {
                    isEndOfHeaders = true;
                    break;
                }

                String headerLine = lineBuffer.toString("UTF-8").trim();
                parseHeaderLine(headerLine, headers);
                lineBuffer.reset();
            } else if (currentByte != '\r') {
                lineBuffer.write(currentByte);
            }
            previousByte = currentByte;
        }

        if (!isEndOfHeaders) {
            throw new IOException("Unexpected end of headers");
        }

        logger.debug("Headers : {}", headers);
        return request.setHeaders(headers);
    }

    private static void parseHeaderLine(String headerLine, Map<String, List<String>> headers) {
        int separatorIndex = headerLine.indexOf(':');
        if (separatorIndex > 0) {
            String key = headerLine.substring(0, separatorIndex).trim();
            String value = headerLine.substring(separatorIndex + 1).trim();

            headers.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }
    }
}