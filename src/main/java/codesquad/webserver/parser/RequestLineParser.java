package codesquad.webserver.parser;

import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.parser.enums.HttpMethod;
import java.io.BufferedReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RequestLineParser {

    private static final Logger logger = LoggerFactory.getLogger(RequestLineParser.class);

    public static HttpRequest parse(BufferedReader in, HttpRequest request) throws IOException {
        String requestLine = in.readLine();
        if (requestLine == null) {
            throw new IOException("Invalid request line");
        }

        String[] parts = requestLine.split(" ");
        if (parts.length != 3) {
            throw new IOException("Invalid request line: " + requestLine);
        }

        HttpMethod method = HttpMethod.find(parts[0]);
        String fullPath = parts[1]; //쿼리스트링 별도 분리 필요
        String path = extractPath(fullPath);
        String httpVersion = parts[2];

        logger.debug("Request Line : {} {} {}", method, fullPath, httpVersion);

        return request.setRequestLine(new RequestLine(method, path, fullPath, httpVersion));
    }

    private static String extractPath(String fullPath){
        int queryStringStart = fullPath.indexOf('?');
        return queryStringStart != -1 ? fullPath.substring(0, queryStringStart) : fullPath;
    }
}
