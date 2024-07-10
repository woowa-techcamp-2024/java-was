package codesquad.http.session;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private static SessionManager instance;

    private final Map<String, String> sessions = new ConcurrentHashMap<>();

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public String createSession(String userId) {
        return sessions.entrySet().stream()
                .filter(entry -> entry.getValue().equals(userId))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseGet(() -> {
                    String sessionId = UUID.randomUUID().toString();
                    sessions.put(sessionId, userId);
                    return sessionId;
                });
    }

    public String findUserId(String sessionId) {
        return sessions.get(sessionId);
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public boolean isValidSession(String sessionId) {
        return sessions.containsKey(sessionId);
    }

    public void clear() {
        sessions.clear();
    }

}
