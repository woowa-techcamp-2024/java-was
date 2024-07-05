package codesquad.webserver.config;

import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.requesthandler.HomeRequestHandler;
import codesquad.webserver.requesthandler.RegisterRequestHandler;
import codesquad.webserver.requesthandler.StaticFileHandler;
import codesquad.webserver.requesthandler.UserCreateRequestHandler;
import codesquad.webserver.router.Router;

public class RouteConfig {
    private final Router router;
    private final FileReader fileReader;

    public RouteConfig(FileReader fileReader) {
        this.router = Router.getInstance();
        this.fileReader = fileReader;
    }

    public void configureRoutes() {
        StaticFileHandler staticFileHandler = new StaticFileHandler(fileReader);


        router.addRoute("*.css", staticFileHandler);

        router.addRoute("*.js", staticFileHandler);

        router.addRoute("*.png", staticFileHandler);
        router.addRoute("*.jpg", staticFileHandler);
        router.addRoute("*.gif", staticFileHandler);

        router.addRoute("/img/*", staticFileHandler);
        router.addRoute("/favicon.ico", staticFileHandler);

        router.addRoute("/index.html", new HomeRequestHandler(fileReader));
        router.addRoute("/register.html", new RegisterRequestHandler(fileReader));
        router.addRoute("/create", new UserCreateRequestHandler(fileReader));
    }
}
