package codesquad.servlet;

import codesquad.domain.model.User;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionStorage {

    private final Map<String, User> sessions = new ConcurrentHashMap<>();

    private SessionStorage() {
    }

    private static class SingletonHolder {
        private static final SessionStorage INSTANCE = new SessionStorage();
    }

    public static SessionStorage getInstance() {
        return SessionStorage.SingletonHolder.INSTANCE;
    }

    public String createSession(User user) {
        String uuid = UUID.randomUUID().toString();
        sessions.put(uuid, user);
        return uuid;
    }

    public void deleteSession(String uuid) {
        sessions.remove(uuid);
    }

    public User getSessionValue(String uuid) {
        return sessions.get(uuid);
    }

    public boolean isExistingSession(String uuid) {
        return sessions.containsKey(uuid);
    }

    public void removeSessionValue(String uuid) {
        sessions.remove(uuid);
    }

    public Optional<User> findByUuid(String uuid) {
        return Optional.ofNullable(sessions.get(uuid));
    }

    public Map<String, User> getSessions() {
        return Collections.unmodifiableMap(sessions);
    }
}
