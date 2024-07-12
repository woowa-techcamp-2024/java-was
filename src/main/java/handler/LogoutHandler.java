package handler;

import http.Cookie;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import session.SessionManager;
import util.RequestContext;

public class LogoutHandler extends MyHandler {

    @Override
    void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        RequestContext.current().getSession().ifPresent(
            s -> SessionManager.getInstance().invalidateSession(s.getSessionId())
        );

        Cookie setCookie = new Cookie("sid", "", null, 0,"/", true);
        httpResponse.getHeader().addCookie(setCookie);
        ResponseValueSetter.redirect(httpRequest, httpResponse, "/index.html");
    }
}
