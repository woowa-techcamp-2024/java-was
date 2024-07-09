package codesquad.application.handler;

import codesquad.webserver.annotation.Handler;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Handler
public class RegistrationHandler {
    private final Logger log = LoggerFactory.getLogger(RegistrationHandler.class);
    @RequestMapping(method = HttpMethod.POST, path="/create")
    public HttpResponse create2(HttpRequest request){
        Map<String, Object> bodyMessage = request.getHttpBody();
        log.debug("body: " + bodyMessage);
        return HttpResponse.createRedirectResponse();
    }
}
