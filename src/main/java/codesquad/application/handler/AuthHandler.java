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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Handler
public class AuthHandler {
    private final Logger log = LoggerFactory.getLogger(RegistrationHandler.class);

    @RequestMapping(method = HttpMethod.POST, path="/login")
    public HttpResponse login(HttpRequest request){
        Map<String, Object> bodyMessage = request.getHttpBody();
        String username = (String) bodyMessage.get("username");
        String password = (String) bodyMessage.get("password");
        log.debug("request body: " + username + " " + password);
        User user = UserDatabase.getInstance().get(username);
        if(user == null || !user.checkPassword(password)){
            return HttpResponse.createRedirectResponse("/login-failed");
        }
        String sessionkey = Session.getInstance().addUser(user);
        HttpResponse response = HttpResponse.createRedirectResponse("/index");
        response.setCookie(sessionkey);
        return response;
    }

    @RequestMapping(method = HttpMethod.POST, path="/logout")
    public HttpResponse logout(HttpRequest request){
        String sid = CookieExtractor.getSid(request);
        log.debug("logout: "+sid);
        User sessionUser = Session.getInstance().getUser(sid);
        User savedUser = UserDatabase.getInstance().get(sessionUser.getUserName());
        log.debug("sessionUser: "+sessionUser + " savedUser: "+savedUser);
        if(!sessionUser.equals(savedUser)){
            return HttpResponse.createNotFoundResponse();
        }
        Session.getInstance().deleteUser(sid);
        HttpResponse response = HttpResponse.createRedirectResponse("/index");
        response.removeCookie();
        return response;
    }
}
