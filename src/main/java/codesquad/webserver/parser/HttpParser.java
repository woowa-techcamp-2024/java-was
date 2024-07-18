package codesquad.webserver.parser;

import codesquad.webserver.httprequest.HttpRequest;
import java.io.BufferedInputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HttpParser {
    private static final Logger logger = LoggerFactory.getLogger(HttpParser.class);

    public static HttpRequest parse(BufferedInputStream in) throws IOException {

        HttpRequest request = new HttpRequest();

        RequestLineParser.parse(in, request);
        HeaderParser.parse(in, request);
        QueryStringParser.parseQueryString(request);
        BodyParser.parse(in, request);

        logger.info("httpRequest : {}", request);
        return request;
    }
}
