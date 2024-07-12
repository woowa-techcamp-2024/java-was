package session;

import java.util.HashMap;
import java.util.Map;

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

    public String getUserId() {
        return (String) getAttribute(USER);
    }

    public void setUserId(String userId) {
        if (isValidUserId(userId) && getUserId() == null) {
            setAttribute(USER, userId);
        } else {
            throw new IllegalArgumentException(
                "Invalid userId: userId must be non-null, non-empty and current userId must be null."
            );
        }
    }

    private boolean isValidUserId(String userId) {
        return userId != null && !userId.isEmpty();
    }
}