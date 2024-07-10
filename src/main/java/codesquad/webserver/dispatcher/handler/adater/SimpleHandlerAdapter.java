package codesquad.webserver.dispatcher.handler.adater;

import codesquad.webserver.annotation.Component;
import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.dispatcher.requesthandler.RequestHandler;

@Component
public class SimpleHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof RequestHandler;
    }

    @Override
    public ModelAndView handle(HttpRequest request, Object handler) {
        if (!supports(handler)) {
            throw new IllegalArgumentException("Handler not supported");
        }

        RequestHandler requestHandler = (RequestHandler) handler;
        HttpResponse response= requestHandler.handle(request);

        return convertToModelAndView(response);
    }

    private ModelAndView convertToModelAndView(HttpResponse response) {
        if (response.getStatusCode() == 302) {
            String redirectUrl = response.getHeaders().get("Location");
            return new ModelAndView("redirect:" + redirectUrl);
        }

        ModelAndView mv = new ModelAndView("defaultView");
        mv.addAttribute("statusCode", response.getStatusCode());
        mv.addAttribute("headers", response.getHeaders());
        mv.addAttribute("body", response.getBody());
        return mv;
    }
}
