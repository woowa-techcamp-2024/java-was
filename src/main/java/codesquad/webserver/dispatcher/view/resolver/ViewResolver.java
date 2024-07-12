package codesquad.webserver.dispatcher.view.resolver;

import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.dispatcher.view.View;
import codesquad.webserver.httprequest.HttpRequest;

public interface ViewResolver {
    View resolveView(ModelAndView modelAndView);
}
