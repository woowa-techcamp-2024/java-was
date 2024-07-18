package codesquad.webserver.parser;

import codesquad.webserver.httprequest.HttpRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class QueryStringParser {

    private static final String CHARSET = "UTF-8";
    private static final Logger logger = LoggerFactory.getLogger(QueryStringParser.class);

    public static Map<String, String> parse(String queryString) {
        Map<String, String> params = new HashMap<>();
        if (queryString == null || queryString.isEmpty()) {
            return params;
        }

        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx > 0) {
                try {
                    String key = URLDecoder.decode(pair.substring(0, idx).trim(), CHARSET);
                    String value = URLDecoder.decode(pair.substring(idx + 1).trim(), CHARSET);
                    params.put(key, value);
                } catch (UnsupportedEncodingException e) {
                    logger.error("Error decoding query string parameter: {}", pair, e);
                }
            }
        }
        return params;
    }

    public static void parseQueryString(HttpRequest request) {
        String fullPath = request.getRequestLine().getFullPath();
        int queryStringStart = fullPath.indexOf('?');

        if (queryStringStart != -1) {
            String queryString = fullPath.substring(queryStringStart + 1);
            Map<String, String> params = parse(queryString);
            request.setParams(params);
            logger.debug("Parsed query string parameters: {}", params);
        } else {
            request.setParams(new HashMap<>());
            logger.debug("No query string parameters found");
        }
    }
}