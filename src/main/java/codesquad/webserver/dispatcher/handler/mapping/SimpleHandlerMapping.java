package codesquad.webserver.dispatcher.handler.mapping;

import codesquad.webserver.annotation.Component;
import codesquad.webserver.httprequest.HttpRequest;
import java.util.HashMap;
import java.util.Map;

@Component
public class SimpleHandlerMapping implements HandlerMapping {

    private final Map<String, Object> handlers = new HashMap<>();

    public void addHandler(String path, Object handler) {
        handlers.put(path, handler);
    }

    @Override
    public Object getHandler(HttpRequest request) {
        return handlers.get(request.getRequestLine().getPath());
    }
}