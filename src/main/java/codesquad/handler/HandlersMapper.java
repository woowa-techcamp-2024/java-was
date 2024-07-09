package codesquad.handler;

import codesquad.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class HandlersMapper {

    private final Map<String, RequestHandler> requestHandlers = new HashMap<>();

    {
        requestHandlers.put("/user/create", UserRegistrationHandler.getInstance());
        requestHandlers.put("/", StaticResourceHandler.getInstance());
    }

    public RequestHandler getRequestHandler(HttpRequest httpRequest) {
        String uri = httpRequest.uri();
        return requestHandlers.entrySet()
                .stream()
                .filter(entry -> uri.startsWith(entry.getKey()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당 요청을 처리할 수 있는 핸들러가 없습니다. uri: " + uri));
    }

}
