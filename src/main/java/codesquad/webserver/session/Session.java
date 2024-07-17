package codesquad.webserver.session;

import codesquad.webserver.db.user.User;

public interface Session {
    String getId();
    User getUser();
    void setAttribute(String name, Object value);
    Object getAttribute(String name);
    void removeAttribute(String name);
    long getCreationTime();
    void invalidate();
}
