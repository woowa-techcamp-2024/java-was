package codesquad.servlet.handler.api;

import codesquad.servlet.SessionStorage;
import codesquad.servlet.handler.Handler;
import codesquad.webserver.http.Cookie;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserLogoutHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationHandler.class);
    private final SessionStorage sessionStorage;

    public UserLogoutHandler(SessionStorage sessionStorage) {
        this.sessionStorage = sessionStorage;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        Cookie cookie = request.getCookie();
        logger.debug("cookie: {}", cookie);
        if (cookie == null || !cookie.getName().equals("sid")) {
            response.unauthorized();
            return;
        }

        sessionStorage.removeSessionValue(cookie.getValue());
        cookie.expire();
        logger.debug("cookie: {}", cookie);
        response.addCookie(cookie);
        response.sendRedirect("/index.html");
    }
}
