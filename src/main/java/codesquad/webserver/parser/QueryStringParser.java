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
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                try {
                    String key = URLDecoder.decode(keyValue[0].trim(), CHARSET);
                    String value = URLDecoder.decode(keyValue[1].trim(), CHARSET);
                    params.put(key, value);
                } catch (UnsupportedEncodingException e) {
                    logger.error("Error decoding query string: {}", e.getMessage());
                }
            }
        }
        return params;
    }

    public static void parseQueryString(HttpRequest request) {
        String url = request.getRequestLine().getFullPath();
        String[] urlParts = url.split("\\?");
        if (urlParts.length == 2) {
            request.setParams(parse(urlParts[1]));
            return;
        }
        request.setParams(new HashMap<>());
    }
}
