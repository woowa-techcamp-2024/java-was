package codesquad.handler;

import codesquad.http.HttpCookies;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.session.SessionManager;

public class UserLogoutHandler extends RequestHandler {

    private static UserLogoutHandler instance = new UserLogoutHandler();

    private final SessionManager sessionManager = SessionManager.getInstance();

    private UserLogoutHandler() {
    }

    public static UserLogoutHandler getInstance() {
        if (instance == null) {
            instance = new UserLogoutHandler();
        }
        return instance;
    }

    @Override
    protected HttpResponse handleGet(HttpRequest httpRequest) {
        sessionManager.removeSession(httpRequest.getCookie("sid"));
        HttpResponse httpResponse = responseGenerator.sendRedirect(httpRequest, "/");
        HttpCookies cookie = new HttpCookies();
        cookie.addCookie("sid", "");
        cookie.addCookie("Path", "/");
        cookie.addCookie("Max-Age", "0");
        httpResponse.addCookie(cookie);
        return httpResponse;
    }
}
