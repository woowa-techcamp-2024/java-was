package http.startline;

import java.util.HashMap;
import java.util.Map;

public class UrlPath {

    public static UrlPath of(String fullPath) {
        return new UrlPath(fullPath);
    }

    private final String fullPath;
    private final String path;
    private final Map<String, String> queryParameters;

    public UrlPath(String fullPath) {
        this.fullPath = fullPath;
        this.queryParameters = parseQuery(fullPath);
        this.path = parsePath(fullPath);
    }

    private String parsePath(String fullPath) {
        String[] parts = fullPath.split("\\?", 2);
        return parts[0];
    }

    private HashMap<String, String> parseQuery(String fullPath) {
        String[] parts = fullPath.split("\\?", 2);
        if (parts.length == 2) {
            return parseQueryParameters(parts[1]);
        }
        return new HashMap<>();
    }

    private HashMap<String, String> parseQueryParameters(String queryString) {
        String[] params = queryString.split("&");
        HashMap<String, String> ret = new HashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("=", 2);
            if (keyValue.length == 2) {
                ret.put(keyValue[0], keyValue[1]);
            } else {
                ret.put(keyValue[0], "");
            }
        }
        return ret;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryParameters() {
        return queryParameters;
    }

    public String getQueryParameter(String key) {
        return queryParameters.get(key);
    }
}
