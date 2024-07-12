package codesquad.webserver.filter;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.httpresponse.HttpResponseBuilder;
import codesquad.webserver.session.Session;
import codesquad.webserver.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@codesquad.webserver.annotation.Filter
public class SessionCheckFilter implements Filter {

    private static final String SESSION_KEY = "SID";
    private static final Logger logger = LoggerFactory.getLogger(SessionCheckFilter.class);

    private final SessionManager sessionManager;

    @Autowired
    public SessionCheckFilter(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) {
        if (isProtectedPath(request.requestLine().path()) && !hasValidSession(request)) {
            logger.debug("세션이 존재하지 않는 사용자 요청: {}", request.requestLine().path());
            HttpResponse redirectResponse = HttpResponseBuilder.redirect("/login").build();
            chain.setResponse(redirectResponse);
        }
    }

    private boolean isProtectedPath(String path) {
        return path.equals("/user/list");
    }

    private boolean hasValidSession(HttpRequest request) {
        Session session = sessionManager.getSession(request.getSessionIdFromRequest());
        logger.error("세션 : {}", session);
        return session != null;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
