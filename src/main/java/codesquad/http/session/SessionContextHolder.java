package codesquad.http.session;

public class SessionContextHolder {

    private static final ThreadLocal<String> context = new ThreadLocal<>();

    private SessionContextHolder() {
    }

    public static void setSessionId(String sessionId) {
        context.set(sessionId);
    }

    public static String getSessionId() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}
