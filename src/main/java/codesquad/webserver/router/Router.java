package codesquad.webserver.router;

import codesquad.webserver.requesthandler.RequestHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Router {

    private static final Map<String, RequestHandler> ROUTES = new HashMap<>();

    private Router() {
    }

    private static class Holder {
        private static final Router INSTANCE = new Router();
    }

    public static Router getInstance() {
        return Holder.INSTANCE;
    }

    public void addRoute(String path, RequestHandler handler) {
        //TODO: fullPath, hadler null 체크 추가
        if (ROUTES.putIfAbsent(path, handler) != null) {
            throw new IllegalStateException("이미 존재하는 경로입니다.: " + path);
        }
    }

    public RequestHandler getHandler(String path) {

        RequestHandler handler = ROUTES.get(path);
        if (handler != null) {
            return handler;
        }

        for (Entry<String, RequestHandler> entry : ROUTES.entrySet()) {
            String routePath = entry.getKey();
            if (isWildcardMatch(routePath, path)) {
                return entry.getValue();
            }
        }

        throw new IllegalArgumentException("핸들러가 존재하지 않습니다 : " + path);
    }

    private boolean isWildcardMatch(String pattern, String path) {
        if (pattern.endsWith("/*")) {
            String prefix = pattern.substring(0, pattern.length() - 2);
            return path.startsWith(prefix);
        }
        if (pattern.startsWith("*.")) {
            String suffix = pattern.substring(1);
            return path.endsWith(suffix);
        }
        return false;
    }

    public void removeRoute(String path) {
        ROUTES.remove(path);
    }
}
