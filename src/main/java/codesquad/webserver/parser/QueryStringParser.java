package codesquad.webserver.parser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class QueryStringParser {

    private static final String CHARSET = "UTF-8";
    private static final Logger logger = LoggerFactory.getLogger(QueryStringParser.class);

    public static Map<String, String> parse(String path) {
        String[] urlParams = path.split("\\?");
        if (urlParams.length == 2) {
            String param = path.split("\\?")[1];
            Map<String, String> params = new HashMap<>();
            for (String info : param.split("&")) {
                String[] keyValue = info.split("=", 2);
                if (keyValue.length == 2) {
                    try {
                        String key = URLDecoder.decode(keyValue[0].trim(), CHARSET);
                        String value = URLDecoder.decode(keyValue[1].trim(), CHARSET);
                        params.put(key, value);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                }
            }
            return params;
        }
        return Map.of();
    }
}
