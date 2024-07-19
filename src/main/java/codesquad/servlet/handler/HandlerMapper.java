package codesquad.servlet.handler;

import codesquad.webserver.http.HttpMethod;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HandlerMapper {

    private static final Map<HttpMethod, Map<String, Handler>> handlers;

    static {
        handlers = new HashMap<>();
        for (HttpMethod httpMethod : HttpMethod.values()) {
            handlers.put(httpMethod, new HashMap<>());
        }
    }

    private HandlerMapper() {
    }

    private static class SingletonHolder {
        private static final HandlerMapper INSTANCE = new HandlerMapper();
    }

    public static HandlerMapper getInstance() {
        return HandlerMapper.SingletonHolder.INSTANCE;
    }

    public void addMapping(HttpMethod httpMethod, String path, Handler handler) {
        handlers.get(httpMethod).put(path, handler);
    }

    public Optional<Handler> findBy(HttpMethod httpMethod, String path) {
        return Optional.ofNullable(handlers.get(httpMethod).get(path));
    }

}
