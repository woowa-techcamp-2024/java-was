package codesquad.servlet.handler.api;

import codesquad.servlet.SessionStorage;
import codesquad.servlet.handler.Handler;
import codesquad.webserver.http.Cookie;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserLogoutHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(UserLogoutHandler.class);
    private final SessionStorage sessionStorage;

    public UserLogoutHandler(SessionStorage sessionStorage) {
        this.sessionStorage = sessionStorage;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        logger.debug("UserLogoutHandler start");
        // 사용자 인증
        Cookie cookie = request.getCookie();

        sessionStorage.removeSessionValue(cookie.getValue());
        cookie.expire();
        logger.debug("cookie: {}", cookie);
        response.addCookie(cookie);
        response.sendRedirect("/index.html");
        logger.debug("UserLogoutHandler end");
    }
}
