package codesquad.webserver.dispatcher.requesthandler;

import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;

public interface RequestHandler {
    ModelAndView handle(HttpRequest request);
}
