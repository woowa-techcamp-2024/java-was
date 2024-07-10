package util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class QueryParserUtil {
    private final static Logger logger = Logger.getLogger(QueryParserUtil.class.getName());


    public static Map<String, String> parseQuery(String queryString) {
        Map<String, String> queryParameters = new HashMap<>();

        if (queryString == null || queryString.isEmpty()) {
            return queryParameters;
        }

        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            try {
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8.name());
                String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name()) : "";
                queryParameters.put(key, value);
            } catch (UnsupportedEncodingException e) {
                System.err.println("Error decoding query parameter: " + e.getMessage());
            }
        }
        return queryParameters;
    }
}
