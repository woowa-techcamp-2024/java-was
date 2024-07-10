package codesquad.webserver.dispatcher.handler.adater;

import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.httprequest.HttpRequest;

public interface HandlerAdapter {
    boolean supports(Object handler);

    ModelAndView handle(HttpRequest request, Object handler);
}
