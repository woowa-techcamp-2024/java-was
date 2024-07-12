package util;

import java.util.Optional;
import session.Session;
import http.startline.UrlPath;

public class RequestContext {
    private static final ThreadLocal<RequestContext> threadLocal = new ThreadLocal<>();

    private final UrlPath urlPath;
    private final Session session;

    private RequestContext(UrlPath urlPath, Session session) {
        this.urlPath = urlPath;
        this.session = session;
    }

    public static RequestContext of(UrlPath urlPath, Session session) {
        RequestContext requestContext = new RequestContext(urlPath, session);
        threadLocal.set(requestContext);
        return requestContext;
    }

    public static RequestContext current() {
        return threadLocal.get();
    }

    public UrlPath getUrlPath() {
        return urlPath;
    }

    public Optional<Session> getSession() {
        // return SessionManager.getInstance().getSession(this.sessionId);
        return Optional.ofNullable(session);
    }
}
