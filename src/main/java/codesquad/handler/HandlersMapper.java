package codesquad.handler;

import codesquad.http.HttpRequest;

import java.util.Map;

public class HandlersMapper {

    private final Map<String, RequestHandler> requestHandlers = Map.of(
            "/user/login", UserLoginHandler.getInstance(),
            "/user/logout", UserLogoutHandler.getInstance(),
            "/user/create", UserRegistrationHandler.getInstance(),
            "/user/list", UserListHandler.getInstance(),
            "/article", ArticleHandler.getInstance(),
            "/", DynamicResourceHandler.getInstance()
    );

    public RequestHandler getRequestHandler(HttpRequest httpRequest) {
        String uri = httpRequest.uri();
        return requestHandlers.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(uri))
                .findAny()
                .map(Map.Entry::getValue)
                .orElse(DynamicResourceHandler.getInstance());
    }

}
