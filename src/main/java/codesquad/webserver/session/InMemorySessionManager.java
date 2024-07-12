package codesquad.webserver.session;

import codesquad.webserver.annotation.Component;
import codesquad.webserver.model.User;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class InMemorySessionManager implements SessionManager {

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private static final long SESSION_TIMEOUT = 30 * 60 * 1000;
    private static final long SESSION_CLEANUP_INTERVAL = 15 * 60 * 1000; // 15분

    public InMemorySessionManager() {
        scheduleSessionCleanup();
    }

    private static class Holder {
        private static final SessionManager INSTANCE = new InMemorySessionManager();
    }

    public static SessionManager getInstance() {
        return InMemorySessionManager.Holder.INSTANCE;
    }

    @Override
    public Session createSession(User user) {
        String sessionId = generateSessionId();
        Session session = new InMemorySession(sessionId, user);
        sessions.put(sessionId, session);
        return session;
    }

    @Override
    public Session getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    @Override
    public void invalidateExpiredSessions() {
        long now = System.currentTimeMillis();
        sessions.entrySet().removeIf(entry ->
                now - entry.getValue().getCreationTime() > SESSION_TIMEOUT
        );
    }

    @Override
    public void invalidateSession(String sessionId) {
        sessions.remove(sessionId);
    }

    @Override
    public void clearAllSessions() {
        sessions.clear();
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString().substring(0, 16);
    }

    private void scheduleSessionCleanup() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::invalidateExpiredSessions, SESSION_CLEANUP_INTERVAL, SESSION_CLEANUP_INTERVAL, TimeUnit.MILLISECONDS);
    }
}
