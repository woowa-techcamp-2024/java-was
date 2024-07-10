package handler;

import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import repository.SessionManager;
import util.RequestContext;

public class LogoutHandler extends MyHandler {

    @Override
    void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        SessionManager.getInstance().invalidateSession(RequestContext.current().getSessionId());
        if(SessionManager.getInstance().getSession(RequestContext.current().getSessionId()) != null){
            // 세션 삭제 실패
            httpResponse.getHeader().addKey("Set-Cookie", setCookieWithSessionId() + "; Max-Age=0");
        }
        ResponseValueSetter.redirect(httpRequest, httpResponse, "/index.html");
    }

    public static String setCookieWithSessionId() {
        int sessionId = RequestContext.current().getSessionId();
        return "sessionId=" + sessionId + "; HttpOnly";
    }
}
