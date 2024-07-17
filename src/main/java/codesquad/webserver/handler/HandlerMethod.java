package codesquad.webserver.handler;

import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import java.lang.reflect.Method;

public class HandlerMethod {
    private final Object handler;
    private final Method method;

    public HandlerMethod(Object handler, Method method) {
        this.handler = handler;
        this.method = method;
    }

    public HttpResponse invoke(HttpRequest request){
        try{
            HttpResponse response = (HttpResponse) this.method.invoke(this.handler, request);
            return response;
        }catch (Exception e){

        }
        return HttpResponse.notFound();
    }

    @Override
    public String toString() {
        return "HandlerMethod{" +
                "handler=" + handler +
                ", method=" + method +
                '}';
    }
}
