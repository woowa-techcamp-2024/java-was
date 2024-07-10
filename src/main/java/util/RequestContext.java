package util;

public class RequestContext {
    private static final ThreadLocal<RequestContext> threadLocal = new ThreadLocal<>();

    private final String urlPath;
    private final int sessionId;

    private RequestContext(String urlPath, int sessionId) {
        this.urlPath = urlPath;
        this.sessionId = sessionId;
    }

    public static RequestContext of(String urlPath, int sessionId) {
        RequestContext requestContext = new RequestContext(urlPath, sessionId);
        threadLocal.set(requestContext);
        return requestContext;
    }

    public static RequestContext current() {
        return threadLocal.get();
    }

    public String getUrlPath() {
        return urlPath;
    }

    public int getSessionId() {
        return sessionId;
    }
}
