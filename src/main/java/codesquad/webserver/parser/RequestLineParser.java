package codesquad.webserver.parser;

import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.parser.enums.HttpMethod;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RequestLineParser {

    private static final Logger logger = LoggerFactory.getLogger(RequestLineParser.class);

    public static HttpRequest parse(BufferedInputStream in, HttpRequest request) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1) {
            if (b == '\r') {
                int next = in.read();
                if (next == '\n') {
                    break;
                }
                baos.write(b);
                baos.write(next);
            } else {
                baos.write(b);
            }
        }

        String requestLine = baos.toString("UTF-8");
        if (requestLine.isEmpty()) {
            throw new IOException("Invalid request line");
        }

        String[] parts = requestLine.split(" ");
        if (parts.length != 3) {
            throw new IOException("Invalid request line: " + requestLine);
        }

        HttpMethod method = HttpMethod.find(parts[0]);
        String fullPath = parts[1];
        String path = extractPath(fullPath);
        String httpVersion = parts[2];

        logger.debug("Request Line : {} {} {}", method, fullPath, httpVersion);

        return request.setRequestLine(new RequestLine(method, path, fullPath, httpVersion));
    }

    private static String extractPath(String fullPath) {
        int queryStringStart = fullPath.indexOf('?');
        return queryStringStart != -1 ? fullPath.substring(0, queryStringStart) : fullPath;
    }
}