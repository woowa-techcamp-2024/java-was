package http.startline;

import java.util.HashMap;
import java.util.Map;
import util.QueryParserUtil;

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

    private Map<String, String> parseQuery(String fullPath) {
        String[] parts = fullPath.split("\\?", 2);
        if (parts.length == 2) {
            return QueryParserUtil.parseQuery(parts[1]);
        }
        return new HashMap<>();
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
