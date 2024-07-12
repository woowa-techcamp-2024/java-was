package codesquad.servlet.handler;

import codesquad.domain.InMemoryUserStorage;
import codesquad.servlet.SessionStorage;
import codesquad.servlet.handler.api.AllUserInfoHandler;
import codesquad.servlet.handler.api.UserInfoHandler;
import codesquad.servlet.handler.api.UserLoginHandler;
import codesquad.servlet.handler.api.UserLogoutHandler;
import codesquad.servlet.handler.api.UserRegistrationHandler;
import codesquad.webserver.http.HttpMethod;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HandlerMapper {

    private final Map<HttpMethod, Map<String, Handler>> handlers;

    public HandlerMapper() {
        handlers = new HashMap<>();
        for (HttpMethod httpMethod : HttpMethod.values()) {
            handlers.put(httpMethod, new HashMap<>());
        }

        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        SessionStorage sessionStorage = new SessionStorage();

        handlers.get(HttpMethod.POST)
                .put("/user/create", new UserRegistrationHandler(
                        inMemoryUserStorage,
                        sessionStorage));

        handlers.get(HttpMethod.POST)
                .put("/user/login", new UserLoginHandler(
                        inMemoryUserStorage,
                        sessionStorage));

        handlers.get(HttpMethod.GET)
                .put("/user/logout", new UserLogoutHandler(sessionStorage));

        handlers.get(HttpMethod.GET)
                .put("/api/user/info", new UserInfoHandler(sessionStorage));

        handlers.get(HttpMethod.GET)
                .put("/api/user/list", new AllUserInfoHandler(inMemoryUserStorage, sessionStorage));
    }

    public Optional<Handler> findBy(HttpMethod httpMethod, String path) {
        return Optional.ofNullable(handlers.get(httpMethod).get(path));
    }

}
