package codesquad.servlet.filter;

import codesquad.servlet.SessionStorage;
import codesquad.servlet.execption.CookieNotFoundException;
import codesquad.servlet.execption.InvalidCookieException;
import codesquad.webserver.http.Cookie;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionAuthFilter {

    private static final Logger logger = LoggerFactory.getLogger(SessionAuthFilter.class);
    private static final List<String> AUTH_URI = List.of("/user/list", "/user/logout", "/api/user/info",
            "/api/user/list", "/article/write.html", "/comment");
    private final SessionStorage sessionStorage;

    public SessionAuthFilter(SessionStorage sessionStorage) {
        this.sessionStorage = sessionStorage;
    }

    public void doFilter(HttpRequest httpRequest, HttpResponse httpResponse) {

        if (!AUTH_URI.contains(httpRequest.getPath().getDefaultPath())) {
            return;
        }

        try {
            Cookie cookie = httpRequest.getCookie();
            if (cookie == null) {
                throw new CookieNotFoundException("쿠키가 존재하지 않습니다.");
            }

            String cookieName = cookie.getName();
            String cookieValue = cookie.getValue();
            if (!cookieName.equals("sid")) {
                throw new CookieNotFoundException(
                        "세션 쿠키가 존재하지 않습니다. Cookie={%s=%s}".formatted(cookieName, cookieValue));
            }

            if (!sessionStorage.isExistingSession(cookieValue)) {
                cookie.expire();
                httpResponse.addCookie(cookie);
                throw new InvalidCookieException(
                        "세션에 존재하지 않는 쿠키값입니다. Cookie={%s=%s}".formatted(cookieName, cookieValue));
            }

        } catch (CookieNotFoundException | InvalidCookieException e) {
            logger.debug("[Authentication Exception]", e);
            httpResponse.unauthorized();
            httpResponse.sendRedirect("/login");
        }
    }

}
