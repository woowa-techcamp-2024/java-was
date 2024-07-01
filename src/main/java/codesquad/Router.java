package codesquad;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, String> routes;

    public Router() {
        routes = new HashMap<>();
    }

    public void addRoute(String url, String content) {
        routes.put(url, content);
    }

    public String getContent(String url) {
        return routes.getOrDefault(url, "<h1>404 Not Found</h1>");
    }
}
