package codesquad.application.handler;

import codesquad.application.database.UserDatabase;
import codesquad.application.domain.User;
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
    public HttpResponse create(HttpRequest request){
        Map<String, Object> bodyMessage = request.getHttpBody();
        String username = (String) bodyMessage.get("username");
        String password = (String) bodyMessage.get("password");
        String nickname = (String) bodyMessage.get("nickname");
        log.debug("request body: " + username + " " + nickname + " " + password);
        User user = new User(username, password, nickname);
        UserDatabase.getInstance().add(username, user);
        return HttpResponse.createRedirectResponse("/index");
    }
}
