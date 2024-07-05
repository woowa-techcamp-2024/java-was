package codesquad.application.handler;

import codesquad.application.domain.User;
import codesquad.webserver.annotation.Handler;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Handler
public class RegistrationHandler {
    private final Logger log = LoggerFactory.getLogger(RegistrationHandler.class);

    @RequestMapping(method = HttpMethod.GET, path = "/create")
    public HttpResponse create(HttpRequest request){
        String queryString = request.getQueryString();
        String[] query = queryString.split("&");
        String userId = query[0].split("=")[1];
        String nickname = query[1].split("=")[1];
        String password = query[2].split("=")[1];
        User user = new User(userId, password, nickname);
        log.debug("user: "+user);
        return HttpResponse.createRedirectResponse();
    }
}
