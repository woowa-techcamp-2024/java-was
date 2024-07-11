package codesquad.webserver.db.session;

import codesquad.webserver.model.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySession implements Session {

    private final String id;
    private final User user;
    private final Map<String, Object> attributes;
    private final long creationTime;

    public InMemorySession(String id, User user) {
        this.id = id;
        this.user = user;
        this.attributes = new ConcurrentHashMap<>();
        this.creationTime = System.currentTimeMillis();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public void invalidate() {
        attributes.clear();
    }
}
