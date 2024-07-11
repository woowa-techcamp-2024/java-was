package codesquad.application.handler;


import codesquad.webserver.annotation.Handler;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.handler.StaticResourceHandler;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;

@Handler
public class ViewHandler {
    @RequestMapping(method = HttpMethod.GET, path = "/")
    public HttpResponse home(HttpRequest request){
        return StaticResourceHandler.handle("/index.html");
    }

    @RequestMapping(method = HttpMethod.GET, path = "/index")
    public HttpResponse index(HttpRequest request){
        return StaticResourceHandler.handle("/index.html");
    }

    @RequestMapping(method = HttpMethod.GET, path = "/registration")
    public HttpResponse registration(HttpRequest request){
        return StaticResourceHandler.handle("/registration/index.html");
    }

    @RequestMapping(method = HttpMethod.GET, path = "/login")
    public HttpResponse login(HttpRequest request){
        return StaticResourceHandler.handle("/login/index.html");
    }

    @RequestMapping(method = HttpMethod.GET, path = "/login-failed")
    public HttpResponse loginFailed(HttpRequest request){
        return StaticResourceHandler.handle("/loginFailed/index.html");
    }

    @RequestMapping(method = HttpMethod.GET, path = "/user/list")
    public HttpResponse userList(HttpRequest request){
        return StaticResourceHandler.handle("/userList/index.html");
    }
}
