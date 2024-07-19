package codesquad.domain.db;

import codesquad.domain.entity.Session;
import codesquad.domain.entity.User;
import codesquad.security.SessionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionDatabase implements Database<Session, User> {
    private final Map<Session, User> sessionDB = new HashMap<>();

    @Override
    public Session insert(User data) {
        Session session = SessionManager.createSession();
        sessionDB.put(session, data);
        return session;
    }

    @Override
    public User selectById(Session id) {
        if (!sessionDB.containsKey(id)) return null;
        return sessionDB.get(id);
    }

    @Override
    public Map<Session, User> selectAll() { return sessionDB; }

    @Override
    public void deleteById(Session id) {
        if (!sessionDB.containsKey(id)) return;
        sessionDB.remove(id);
    }
}
