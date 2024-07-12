package codesquad.webserver.dispatcher.handler.adater;

import codesquad.webserver.annotation.Component;
import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.dispatcher.view.ModelKey;
import codesquad.webserver.dispatcher.view.ViewName;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.dispatcher.requesthandler.RequestHandler;
import codesquad.webserver.httpresponse.HttpResponseBuilder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SimpleHandlerAdapter implements HandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SimpleHandlerAdapter.class);

    @Override
    public boolean supports(Object handler) {
        return handler instanceof RequestHandler;
    }

    @Override
    public ModelAndView handle(HttpRequest request, Object handler) {
        if (!supports(handler)) {
            logger.error("Handler not supported");
            ModelAndView mv = new ModelAndView(ViewName.EXCEPTION_VIEW);
            mv.addAttribute(ModelKey.STATUS_CODE, 404);
            return mv;
        }

        RequestHandler requestHandler = (RequestHandler) handler;
        return requestHandler.handle(request);
    }
}
