package codesquad.servlet.handler;

import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;

public interface Handler {

    void service(HttpRequest request, HttpResponse response);

}
