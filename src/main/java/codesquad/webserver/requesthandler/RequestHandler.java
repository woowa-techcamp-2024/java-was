package codesquad.webserver.requesthandler;

import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;

public interface RequestHandler {
    HttpResponse handle(HttpRequest request);
}
