package codesquad.webserver.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonParser {
    public static Map<String, Object> parseForm(String queryString) {
        Map<String, Object> result = new HashMap<>();

        if (queryString == null || queryString.isEmpty()) {
            return result;
        }

        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx > 0) {
                String key = decodeUrlComponent(pair.substring(0, idx));
                String value = decodeUrlComponent(pair.substring(idx + 1));
                result.put(key, value);
            }
        }

        return result;
    }

    private static String decodeUrlComponent(String component) {
        try {
            return URLDecoder.decode(component, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            // UTF-8은 모든 Java 구현에서 지원되므로 이 예외는 발생하지 않아야 합니다.
            throw new RuntimeException("UTF-8 is not supported", e);
        }
    }

    // Content-Type: application/json
    private static final Pattern OBJECT_PATTERN = Pattern.compile("\\{(.*?)\\}");
    private static final Pattern KEY_VALUE_PATTERN = Pattern.compile("\"(.*?)\"\\s*:\\s*(\".*?\"|\\{.*?\\}|-?\\d+(\\.\\d+)?|true|false|null)");

    public static Map<String, Object> parseJson(String jsonString) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonString = jsonString.trim();
        Matcher objectMatcher = OBJECT_PATTERN.matcher(jsonString);
        if(objectMatcher.find()){
            String objectContent = objectMatcher.group(1);
            Matcher keyValueMatcher = KEY_VALUE_PATTERN.matcher(objectContent);
            while(keyValueMatcher.find()){
                String key = keyValueMatcher.group(1);
                String value = keyValueMatcher.group(2);
                jsonMap.put(key, parseJsonValue(value));
            }
        }
        return jsonMap;
    }

    private static Object parseJsonValue(String value){
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        } else if (value.startsWith("{") && value.endsWith("}")) {
            return parseJsonValue(value);
        } else if (value.equals("true") || value.equals("false")) {
            return Boolean.parseBoolean(value);
        } else if (value.equals("null")) {
            return null;
        } else{
            try {
                if (value.contains(".")) {
                    return Double.parseDouble(value);
                } else {
                    return Long.parseLong(value);
                }
            } catch (NumberFormatException e) {
                return value;
            }
        }
    }
}
