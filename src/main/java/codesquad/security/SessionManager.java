package codesquad.security;

import codesquad.domain.entity.Session;
import codesquad.domain.entity.User;
import codesquad.http.HttpRequest;
import codesquad.http.element.HttpHeader;

import java.util.Map;
import java.util.UUID;

import static codesquad.Main.sessionDatabase;

public class SessionManager {
    public static Session createSession() {
        return new Session(UUID.randomUUID().toString());
    }

    public static Session findSession(HttpRequest httpRequest) {
        HttpHeader header = httpRequest.getHeaders().getHeader("Cookie");
        if (header == null) return null;
        return new Session(header.getHeaderValue("SID"));
    }

    public static boolean isExist(Session id) {
        Map<Session, User> sessionUserMap = sessionDatabase.selectAll();
        if (sessionUserMap == null) return false;
        return sessionUserMap.containsKey(id);
    }
}
