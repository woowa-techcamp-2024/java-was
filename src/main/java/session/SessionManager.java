package session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final SessionManager instance = new SessionManager();
    private final Map<Integer, Session> sessions;
    private int nextSessionId;

    private SessionManager() {
        sessions = new ConcurrentHashMap<>();
        nextSessionId = 1;
    }

    public static SessionManager getInstance() {
        return instance;
    }

    public synchronized Session createSession() {
        while (sessions.containsKey(nextSessionId)) {
            nextSessionId++;
        }
        Session session = new Session(nextSessionId);
        sessions.put(nextSessionId, session);
        nextSessionId++;
        return session;
    }

    public Session getSession(Integer sessionId) {
        if (sessionId == null) {
            return null;
        }

        return sessions.get(sessionId);
    }

    public synchronized void invalidateSession(int sessionId) {
        sessions.remove(sessionId);
    }

    public synchronized Session findOrCreateEmptySession() {
        for (int id = 1; id < nextSessionId; id++) {
            if (!sessions.containsKey(id)) {
                Session session = new Session(id);
                sessions.put(id, session);
                return session;
            }
        }
        return createSession();
    }
}