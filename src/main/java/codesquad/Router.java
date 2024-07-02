package codesquad;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Router {
    private final Map<String, String> routes;

    public Router() {
        routes = new ConcurrentHashMap<>();
    }

    public void addRoute(String url, String content) {
        routes.put(url, content);
    }

    public String getContent(String url) {
        return routes.getOrDefault(url, "<h1>404 Not Found</h1>");
    }
}
