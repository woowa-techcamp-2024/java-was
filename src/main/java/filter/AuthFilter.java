package filter;

import exception.InternalServerError;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import http.startline.RequestLine;
import java.io.IOException;
import java.util.Set;
import org.slf4j.Logger;
import util.LoggerUtil;
import util.RequestContext;
import util.StaticPage;

public class AuthFilter implements Filter {

    private static final Logger logger = LoggerUtil.getLogger();
    private static final Set<String> authCheckUrl = Set.of(
        "/user/list", "/create", "/writePage", StaticPage.articleWritePage
    );

    @Override
    public void init() {

    }

    @Override
    public void doFilter(HttpRequest httpRequest, HttpResponse httpResponse,
        FilterChain filterChain) throws IOException {

        // 해당하는 url인지 확인
        RequestLine startLine = (RequestLine) httpRequest.getStartLine();
        if (!authCheckUrl.contains(startLine.getUrlPath().getPath())) {
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }

        RequestContext requestContext = RequestContext.current();
        requestContext.getSession().ifPresentOrElse(
            session -> {
                if (session.getUser() == null) {
                    ResponseValueSetter.redirect(httpRequest, httpResponse, StaticPage.loginPage);
                } else {
                    try {
                        filterChain.doFilter(httpRequest, httpResponse);
                    } catch (IOException e) {
                        ResponseValueSetter.failRedirect(httpResponse, new InternalServerError());
                        logger.error("Error: {}", e.getMessage());
                    }
                }
            },
            () -> ResponseValueSetter.redirect(httpRequest, httpResponse, StaticPage.loginPage)
        );
    }

    @Override
    public void destroy() {

    }
}
