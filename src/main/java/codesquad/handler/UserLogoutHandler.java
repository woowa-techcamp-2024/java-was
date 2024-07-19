package codesquad.handler;

import codesquad.database.SessionDatabase;
import codesquad.http.HttpStatus;
import codesquad.http.request.HttpRequest;
import codesquad.http.response.HttpResponse;

import java.util.Map;

public class UserLogoutHandler implements Handler {


    private static final SessionDatabase sessionDatabase = new SessionDatabase();

    @Override
    public HttpResponse handle(HttpRequest request) throws RuntimeException {
        if(request.method().equals("GET")) {
            return doPost(request);
        }
        return new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED, "text", new byte[0]);
    }

    public HttpResponse doPost(HttpRequest request) {
        HttpResponse response = new HttpResponse("/");
        String sid = getSid(request.headers());
        if(sid != null) {
            sessionDatabase.removeSession(sid);
        }
        expireCookie(response, sid);
        return response;
    }

    private void expireCookie(HttpResponse response, String sid) {
        response.addHeader("Set-Cookie", "sid=" + sid + "Path=/; Max-Age=0");
    }

    private String getSid(Map<String, String> map) {
        return map.get("Cookie").split("=")[1];
    }

}
