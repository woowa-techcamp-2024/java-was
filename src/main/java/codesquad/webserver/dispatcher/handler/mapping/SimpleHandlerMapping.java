package codesquad.webserver.dispatcher.handler.mapping;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.annotation.Component;
import codesquad.webserver.db.user.UserDatabaseFactory;
import codesquad.webserver.dispatcher.requesthandler.HomeRequestHandler;
import codesquad.webserver.dispatcher.requesthandler.LoginRequestHandler;
import codesquad.webserver.dispatcher.requesthandler.LogoutRequestHandler;
import codesquad.webserver.dispatcher.requesthandler.RegisterRequestHandler;
import codesquad.webserver.dispatcher.requesthandler.UserCreateRequestHandler;
import codesquad.webserver.dispatcher.requesthandler.UserListHandler;
import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.session.InMemorySessionManager;
import codesquad.webserver.staticresouce.StaticResourceHandler;
import java.util.HashMap;
import java.util.Map;

@Component
public class SimpleHandlerMapping implements HandlerMapping {

    private final Map<String, Object> handlers = new HashMap<>();
    private final StaticResourceHandler staticResourceHandler;
    private final FileReader fileReader;

    private final UserListHandler userListHandler;
    private final UserCreateRequestHandler userCreateRequestHandler;
    private final HomeRequestHandler homeRequestHandler;
    private final LoginRequestHandler loginRequestHandler;
    private final RegisterRequestHandler registerRequestHandler;
    private final LogoutRequestHandler logoutRequestHandler;

    @Autowired
    public SimpleHandlerMapping(StaticResourceHandler staticResourceHandler, FileReader fileReader,
                                UserListHandler userListHandler, UserCreateRequestHandler userCreateRequestHandler,
                                HomeRequestHandler homeRequestHandler, LoginRequestHandler loginRequestHandler,
                                RegisterRequestHandler registerRequestHandler, LogoutRequestHandler logoutRequestHandler) {
        this.staticResourceHandler = staticResourceHandler;
        this.fileReader = fileReader;
        this.userListHandler = userListHandler;
        this.userCreateRequestHandler = userCreateRequestHandler;
        this.homeRequestHandler = homeRequestHandler;
        this.loginRequestHandler = loginRequestHandler;
        this.registerRequestHandler = registerRequestHandler;
        this.logoutRequestHandler = logoutRequestHandler;
        initHandlers();
    }

    private void initHandlers() {
        handlers.put("", homeRequestHandler);
        handlers.put("/", homeRequestHandler);
        handlers.put("/index.html", homeRequestHandler);
        handlers.put("/register", registerRequestHandler);
        handlers.put("/create", userCreateRequestHandler);
        handlers.put("/login", loginRequestHandler);
        handlers.put("/logout", logoutRequestHandler);
        handlers.put("/user/list", userListHandler);
    }


    @Override
    public Object getHandler(HttpRequest request) {
        return handlers.get(request.requestLine().path());
    }
}
