package codesquad.webserver.parser;

import codesquad.webserver.httprequest.HttpRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HttpParser {
    private static final Logger logger = LoggerFactory.getLogger(HttpParser.class);

    public static HttpRequest parse(BufferedReader in) throws IOException {

        RequestLine requestLine = RequestLineParser.parse(in);
        Map<String, String> headers = HeaderParser.parse(in);
        Map<String, String> params = QueryStringParser.parseQueryString(requestLine.fullPath());
        String body = BodyParser.parse(in, headers);

        return new HttpRequest(requestLine, headers, params, body);
    }
}
