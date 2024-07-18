package codesquad.http.session;

import codesquad.model.User;

public class SessionContextHolder {

    private static final ThreadLocal<SessionContext> context = new ThreadLocal<>();

    private SessionContextHolder() {
    }

    public static void setContext(String sessionId, User user) {
        SessionContext sessionContext = new SessionContext(sessionId, user);
        context.set(sessionContext);
    }

    public static SessionContext getContext() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}
