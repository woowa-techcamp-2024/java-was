package codesquad;

import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.WebServer;
import codesquad.webserver.config.RouteConfig;
import codesquad.webserver.dispatcher.HttpRequestDispatcher;
import codesquad.webserver.httpresponse.HttpResponseWriter;


public class Main {
    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        init();
        WebServer webServer = new WebServer(PORT,
                THREAD_POOL_SIZE,
                new HttpRequestDispatcher(new HttpResponseWriter()));
        webServer.start();
    }

    private static void init() {
        RouteConfig routeConfig = new RouteConfig(new FileReader());
        routeConfig.configureRoutes();
    }
}
