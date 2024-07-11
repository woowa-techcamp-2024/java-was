package codesquad.webserver.filter;

import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.httpresponse.HttpResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@codesquad.webserver.annotation.Filter
public class SessionCheckFilter implements Filter {

    private static final String SESSION_KEY = "SID";
    private static final Logger logger = LoggerFactory.getLogger(SessionCheckFilter.class);

    @Override
    public HttpResponse doFilter(HttpRequest request) {
        if (isProtectedPath(request.requestLine().path()) && !hasValidSession(request)) {
            return HttpResponseBuilder.buildNotFoundResponse(); //TODO: 권한 없음 페이지 변경
        }
        return null; // 필터 통과
    }

    private boolean isProtectedPath(String path) {
        return false; //TODO: 접근 거부 경로 추가
    }

    private boolean hasValidSession(HttpRequest request) {
        return request.headers().containsKey(SESSION_KEY);
    }


}
