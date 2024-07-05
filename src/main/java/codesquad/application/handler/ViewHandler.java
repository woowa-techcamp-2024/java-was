package codesquad.application.handler;


import codesquad.webserver.annotation.Handler;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.handler.StaticResourceHandler;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;

@Handler
public class ViewHandler {
    @RequestMapping(method = HttpMethod.GET, path = "/index")
    public HttpResponse index(HttpRequest request){
        return StaticResourceHandler.handle("/index.html");
    }

    @RequestMapping(method = HttpMethod.GET, path = "/registration")
    public HttpResponse registration(HttpRequest request){
        return StaticResourceHandler.handle("/registration/index.html");

    }
}
