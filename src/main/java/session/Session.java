package session;

import java.util.HashMap;
import java.util.Map;
import model.User;

public class Session {

    public static final String USER = "user";
    private final int sessionId;
    private final Map<String, Object> attributes;

    public Session(int sessionId) {
        this.sessionId = sessionId;
        this.attributes = new HashMap<>();
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    public User getUser() {
        return (User) getAttribute(USER);
    }

    public void setUser(User user) {
        setAttribute(USER, user);
    }
}