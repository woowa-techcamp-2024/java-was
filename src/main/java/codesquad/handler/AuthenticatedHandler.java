package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.session.SessionContext;
import codesquad.http.session.SessionContextHolder;

import java.util.Objects;

public abstract class AuthenticatedHandler extends RequestHandler {

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        SessionContext sessionContext = SessionContextHolder.getContext();
        if (Objects.isNull(sessionContext.user())) {
            return responseGenerator.sendRedirect(httpRequest, "/login");
        }
        return super.handle(httpRequest);
    }
}
