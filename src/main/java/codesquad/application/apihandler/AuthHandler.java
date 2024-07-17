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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

@ApiHandler
public class AuthHandler {
    private final Logger log = LoggerFactory.getLogger(RegistrationHandler.class);
    private final UserDataHandler userDb;

    public AuthHandler(
            @Specify("UserDataHandlerJdbc") UserDataHandler userDb) {
        this.userDb = userDb;
    }

    @RequestMapping(method = HttpMethod.POST, path="/login")
    public HttpResponse login(HttpRequest request){
        Map<String, Object> bodyMessage = request.getHttpBody();
        String username = (String) bodyMessage.get("username");
        String password = (String) bodyMessage.get("password");
        log.debug("request body: " + username + " " + password);
        Optional<User> user = userDb.getByUsername(username);
        if(user.isEmpty() || !user.get().checkPassword(password)){
            return HttpResponse.redirect("/login-failed");
        }
        String sessionkey = SessionManager.getInstance().addUser(user.get());
        HttpResponse response = HttpResponse.redirect("/index");
        response.setCookie(sessionkey);
        return response;
    }

    @RequestMapping(method = HttpMethod.POST, path="/logout")
    public HttpResponse logout(HttpRequest request){
        String sid = CookieExtractor.getSid(request);
        log.debug("[logout] sid: "+sid);
        User sessionUser = SessionManager.getInstance().getUser(sid);
        Optional<User> savedUserOptional = userDb.getByUsername(sessionUser.getUsername());
        if(savedUserOptional.isEmpty()){
            return HttpResponse.badRequest();
        }
        User savedUser = savedUserOptional.get();
        log.debug("[logout] " + "sessionUser: " + sessionUser + "saveUser: "+savedUser);
        if(!sessionUser.equals(savedUser)){
            return HttpResponse.notFound();
        }
        SessionManager.getInstance().deleteUser(sid);
        HttpResponse response = HttpResponse.redirect("/index");
        response.removeCookie();
        return response;
    }
}
