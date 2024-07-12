package codesquad.webserver.filter;

import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;

public interface Filter {
    void doFilter(HttpRequest request, HttpResponse response, FilterChain filterChain);
    default int getOrder() {
        return 0;
    }
}
