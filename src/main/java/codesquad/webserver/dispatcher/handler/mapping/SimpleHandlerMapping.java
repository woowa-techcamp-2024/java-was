package codesquad.webserver.dispatcher.handler.mapping;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.annotation.Component;
import codesquad.webserver.db.session.InMemorySessionManager;
import codesquad.webserver.db.user.UserDatabaseFactory;
import codesquad.webserver.dispatcher.requesthandler.LoginRequestHandler;
import codesquad.webserver.dispatcher.requesthandler.UserCreateRequestHandler;
import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.staticresouce.StaticResourceHandler;
import java.util.HashMap;
import java.util.Map;

@Component
public class SimpleHandlerMapping implements HandlerMapping {

    private final Map<String, Object> handlers = new HashMap<>();
    private final StaticResourceHandler staticResourceHandler;
    private final FileReader fileReader;

    @Autowired
    public SimpleHandlerMapping(StaticResourceHandler staticResourceHandler, FileReader fileReader) {
        this.staticResourceHandler = staticResourceHandler;
        this.fileReader = fileReader;
        initHandlers();
    }

    private void initHandlers() {
        handlers.put("/create", new UserCreateRequestHandler(fileReader));
        handlers.put("/login", new LoginRequestHandler(fileReader, UserDatabaseFactory.getInstance(),
                InMemorySessionManager.getInstance()));
    }


    @Override
    public Object getHandler(HttpRequest request) {
        return handlers.get(request.requestLine().path());
    }
}
