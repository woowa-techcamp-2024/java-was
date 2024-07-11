package codesquad.application.handler;


import codesquad.application.database.UserDatabase;
import codesquad.application.domain.User;
import codesquad.application.session.CookieExtractor;
import codesquad.application.session.Session;
import codesquad.webserver.annotation.Handler;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.util.JsonStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;


@Handler
public class UserHandler {
    private final Logger log = LoggerFactory.getLogger(RegistrationHandler.class);

    @RequestMapping(method = HttpMethod.GET, path="/me")
    public HttpResponse me(HttpRequest request){
        String sid = CookieExtractor.getSid(request);
        if(sid == null){
            return HttpResponse.createOkResponse("NO_USER");
        }
        User user = Session.getInstance().getUser(sid);
        return HttpResponse.createOkResponse(user.getNickname());
    }

    @RequestMapping(method = HttpMethod.GET, path="/api/users/list")
    public HttpResponse getUserList(HttpRequest request){
        Collection<User> users = UserDatabase.getInstance().getUsers();
        String responseBody = JsonStringConverter.collectionToJsonString(users);
        log.debug("users/list: " + responseBody);
        return HttpResponse.createOkResponse(responseBody);
    }
}
