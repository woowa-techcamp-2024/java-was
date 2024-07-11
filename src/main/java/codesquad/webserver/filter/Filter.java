package codesquad.webserver.filter;

import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;

public interface Filter {
    HttpResponse doFilter(HttpRequest request);
    default int getOrder() {
        return 0;
    }
}
