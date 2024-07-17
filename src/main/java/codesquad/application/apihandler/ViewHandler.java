package codesquad.application.apihandler;


import codesquad.webserver.annotation.ApiHandler;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.handler.StaticResourceHandler;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;

@ApiHandler
public class ViewHandler {
    @RequestMapping(method = HttpMethod.GET, path = "/")
    public HttpResponse home(HttpRequest request){
        return StaticResourceHandler.handle("/index.html");
    }

    @RequestMapping(method = HttpMethod.GET, path = "/index")
    public HttpResponse index(HttpRequest request){
        return StaticResourceHandler.handle("/index.html");
    }

    @RequestMapping(method = HttpMethod.GET, path = "/main")
    public HttpResponse mainPage(HttpRequest request){
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


    @RequestMapping(method = HttpMethod.GET, path = "/article")
    public HttpResponse article(HttpRequest request){
        return StaticResourceHandler.handle("/article/index.html");
    }


    @RequestMapping(method = HttpMethod.GET, path = "/write.html")
    public HttpResponse articleHtml(HttpRequest request){
        return StaticResourceHandler.handle("/article/index.html");
    }
}
