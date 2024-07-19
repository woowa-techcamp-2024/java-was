package codesquad.handler;

import codesquad.util.ContentTypeMapper;
import codesquad.util.DirectoryMapper;

import java.util.Map;
import java.util.Optional;

public class HandlerMapper {

    private static final Map<String, Handler> handlerMapper = Map.of(
        "/user/create", new UserCreateHandler(),
        "/user/login", new UserLoginHandler(),
        "/user/logout", new UserLogoutHandler(),
        "/user/list", new UserListHandler(),
        "/index.html", new MainPageHandler(),
        "/write", new WritePageHandler()
    );

    private HandlerMapper() {
    }

    public static Handler findHandler(String uri) {

        Optional<Handler> handler = handlerMapper.entrySet().stream()
                .filter(entry -> uri.equals(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst();

        if(handler.isPresent()){
            return handler.get();
        }

        if(DirectoryMapper.isDirectory(uri)){
            return findHandler(DirectoryMapper.getStaticResourcePath(uri));
        }

        if (ContentTypeMapper.supports(uri)) {
            return new StaticResourceHandler();
        }
        return null;
    }

}