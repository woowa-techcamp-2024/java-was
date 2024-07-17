package codesquad.webserver.handler;

import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicResourceHandler implements ApiHandler {
    private final Logger log = LoggerFactory.getLogger(DynamicResourceHandler.class);
    private Map<HttpMethod, Map<String, HandlerMethod>> routes = new HashMap<>();

    {
        for(HttpMethod httpMethod : HttpMethod.values()){
            routes.put(httpMethod, new HashMap<>());
        }
    }
    public HttpResponse handle(HttpRequest request){
        log.info("DynamicResourceHandler handle");
        Map<String, HandlerMethod> pathToHandlerMethod = routes.get(request.getHttpMethod());
        if(pathToHandlerMethod == null){
            return HttpResponse.methodNotAllowed();
        }
        HandlerMethod handlerMethod = pathToHandlerMethod.get(request.getPath());
        if(handlerMethod == null){
            return handle4xxError(request);
        }
        return handlerMethod.invoke(request);
    }

    private HttpResponse handle4xxError(HttpRequest request) {
        boolean pathExists = routes.values().stream().anyMatch(pathMap -> pathMap.containsKey(request.getPath()));
        if(pathExists){
            return HttpResponse.methodNotAllowed();
        }
        return HttpResponse.notFound();
    }

    public boolean canHandle(HttpRequest request){
        return routes.values().stream().anyMatch(pathMap -> pathMap.containsKey(request.getPath()));
    }

    public void addRoute(HttpMethod httpMethod, String path, HandlerMethod handlerMethod){
        routes.get(httpMethod).put(path, handlerMethod);
    }

    @Override
    public String toString() {
        return "Router{" +
                "routes=" + routes +
                '}';
    }
}
