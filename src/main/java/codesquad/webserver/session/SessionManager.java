package codesquad.webserver.session;

import codesquad.webserver.model.User;

public interface SessionManager {
    Session createSession(User user);
    Session getSession(String sessionId);
    void removeSession(String sessionId);
    void invalidateExpiredSessions();
    void clearAllSessions();
    void invalidateSession(String sessionId);
}
