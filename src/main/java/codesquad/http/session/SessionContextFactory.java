package codesquad.http.session;

import codesquad.http.HttpRequest;

public class SessionContextFactory {

    private static SessionContextFactory instance;

    private SessionContextFactory() {
    }

    public static SessionContextFactory getInstance() {
        if (instance == null) {
            instance = new SessionContextFactory();
        }
        return instance;
    }

    public void createSessionContext(HttpRequest httpRequest) {
        String sessionId = httpRequest.getCookie("sid");
        SessionContextHolder.setSessionId(sessionId);
    }
}
