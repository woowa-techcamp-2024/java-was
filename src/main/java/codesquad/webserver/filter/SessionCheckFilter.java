package codesquad.webserver.filter;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.httpresponse.HttpResponseBuilder;
import codesquad.webserver.session.Session;
import codesquad.webserver.session.SessionManager;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@codesquad.webserver.annotation.Filter
public class SessionCheckFilter implements Filter {

    private static final String SESSION_KEY = "SID";
    private static final Logger logger = LoggerFactory.getLogger(SessionCheckFilter.class);
    private static final Set<String> PROTECTED_PATH = new HashSet<>(Set.of(
            "/user/list",
            "/write"));

    private final SessionManager sessionManager;

    @Autowired
    public SessionCheckFilter(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) {
        if (isProtectedPath(request.getRequestLine().getPath()) && !hasValidSession(request)) {
            logger.debug("세션이 존재하지 않는 사용자 요청: {}", request.getRequestLine().getPath());
            HttpResponse redirectResponse = HttpResponseBuilder.redirect("/login").build();
            chain.setResponse(redirectResponse);
        }
    }

    private boolean isProtectedPath(String path) {
        return PROTECTED_PATH.contains(path);
    }

    private boolean hasValidSession(HttpRequest request) {
        Session session = sessionManager.getSession(request.getSessionIdFromRequest());
        return session != null;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
