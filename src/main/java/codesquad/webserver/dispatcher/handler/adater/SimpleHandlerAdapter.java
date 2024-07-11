package codesquad.webserver.dispatcher.handler.adater;

import codesquad.webserver.annotation.Component;
import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.dispatcher.requesthandler.RequestHandler;
import codesquad.webserver.httpresponse.HttpResponseBuilder;
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
            return convertToModelAndView(HttpResponseBuilder.buildNotFoundResponse());
        }

        RequestHandler requestHandler = (RequestHandler) handler;
        HttpResponse response= requestHandler.handle(request);

        return convertToModelAndView(response);
    }

    private ModelAndView convertToModelAndView(HttpResponse response) {

        ModelAndView mv;

        if (response.getStatusCode() == 302) {
            String redirectUrl = response.getHeaders().get("Location").get(0);
            mv =  new ModelAndView("redirect:" + redirectUrl);
        } else if (response.getStatusCode() == 404) {
            mv = new ModelAndView("exception");
        } else {
            mv = new ModelAndView("defaultView");
        }

        mv.addAttribute("statusCode", response.getStatusCode());
        mv.addAttribute("headers", response.getHeaders());
        mv.addAttribute("body", response.getBody());
        return mv;
    }
}
