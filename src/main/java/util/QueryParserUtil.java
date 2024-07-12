package util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class QueryParserUtil {
    private final static Logger logger = LoggerUtil.getLogger();


    public static Map<String, String> parseQuery(String queryString) {
        if (queryString == null || queryString.isEmpty()) {
            return new HashMap<>();
        }

        return Arrays.stream(queryString.split("&"))
            .map(pair -> pair.split("=", 2))
            .collect(Collectors.toMap(
                keyValue -> URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8),
                keyValue -> keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8) : "",
                (value1, value2) -> value1 // In case of duplicate keys, keep the first one
            ));
    }
}
