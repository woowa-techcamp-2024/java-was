package codesquad.application.apihandler;

import codesquad.application.datahandler.UserDataHandler;
import codesquad.application.domain.User;
import codesquad.webserver.annotation.ApiHandler;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.annotation.Specify;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@ApiHandler
public class RegistrationHandler {
    private final Logger log = LoggerFactory.getLogger(RegistrationHandler.class);
    private final UserDataHandler userDb;

    public RegistrationHandler(
            @Specify("UserDataHandlerJdbc") UserDataHandler userDb) {
        this.userDb = userDb;
    }

    @RequestMapping(method = HttpMethod.POST, path="/create")
    public HttpResponse create(HttpRequest request){
        Map<String, Object> bodyMessage = request.getHttpBody();
        String username = (String) bodyMessage.get("username");
        String password = (String) bodyMessage.get("password");
        String nickname = (String) bodyMessage.get("nickname");
        log.debug("request body: " + username + " " + nickname + " " + password);
        User user = new User(username, password, nickname);
        userDb.insert(user);
        return HttpResponse.redirect("/index");
    }
}
