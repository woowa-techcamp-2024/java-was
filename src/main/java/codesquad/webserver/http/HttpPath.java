package codesquad.webserver.http;

import codesquad.servlet.handler.resource.SupportFileExtension;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class HttpPath {

    private final String defaultPath;
    private final Map<String, String> queryString;

    private HttpPath(String defaultPath, Map<String, String> queryString) {
        this.defaultPath = defaultPath;
        this.queryString = queryString;
    }

    public static HttpPath of(String defaultPath, Map<String, String> queryString) {
        return new HttpPath(defaultPath, queryString);
    }

    public static HttpPath ofOnlyDefaultPath(String defaultPath) {
        return new HttpPath(defaultPath, Map.of());
    }

    public String getDefaultPath() {
        return defaultPath;
    }

    public boolean isDirectoryPath() {
        return !hasFileExtension();
    }

    public boolean isOnlyDefaultPath() {
        return hasDefaultPath() && !hasQueryString();
    }

    private boolean hasDefaultPath() {
        return defaultPath != null && !defaultPath.isEmpty();
    }

    private boolean hasQueryString() {
        return queryString != null && !queryString.isEmpty();
    }

    public boolean hasFileExtension() {
        return Arrays.stream(SupportFileExtension.values())
                .anyMatch(extension -> defaultPath.contains(extension.getName()));
    }

    public Map<String, String> getQueryString() {
        return Collections.unmodifiableMap(queryString);
    }

    @Override
    public String toString() {
        return "path={defaultPath='" + defaultPath + '\'' +
                ", queryString=" + queryString + "}";
    }
}
