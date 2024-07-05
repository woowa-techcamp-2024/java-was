package codesquad.webserver.handler;

import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;

public interface Handler {
    boolean canHandle(HttpRequest request);
    HttpResponse handle(HttpRequest request);
}
