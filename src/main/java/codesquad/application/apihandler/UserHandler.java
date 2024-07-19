package codesquad.application.apihandler;


import codesquad.application.datahandler.UserDataHandler;
import codesquad.application.domain.User;
import codesquad.application.session.CookieExtractor;
import codesquad.application.session.SessionManager;
import codesquad.webserver.annotation.ApiHandler;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.annotation.Specify;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.util.JsonStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@ApiHandler
public class UserHandler {
    private final Logger log = LoggerFactory.getLogger(RegistrationHandler.class);
    private final UserDataHandler userDb;

    public UserHandler(
            @Specify("UserDataHandlerCsv") UserDataHandler userDb) {
        this.userDb = userDb;
    }

    @RequestMapping(method = HttpMethod.GET, path="/me")
    public HttpResponse me(HttpRequest request){
        String sid = CookieExtractor.getSid(request);
        if(sid == null){
            return HttpResponse.ok("NO_USER");
        }
        User user = SessionManager.getInstance().getUser(sid);
        return HttpResponse.ok(user.getNickname());
    }

    @RequestMapping(method = HttpMethod.GET, path="/api/users/list")
    public HttpResponse getUserList(HttpRequest request){
        List<User> users = userDb.getAll();
        String responseBody = JsonStringConverter.collectionToJsonString(users);
        log.debug("users/list: " + responseBody);
        return HttpResponse.ok(responseBody);
    }
}
