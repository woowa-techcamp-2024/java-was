package codesquad.webserver.dispatcher;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.annotation.Component;
import codesquad.webserver.dispatcher.handler.adater.HandlerAdapter;
import codesquad.webserver.dispatcher.handler.mapping.HandlerMapping;
import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.dispatcher.view.ModelKey;
import codesquad.webserver.dispatcher.view.View;
import codesquad.webserver.dispatcher.view.ViewName;
import codesquad.webserver.dispatcher.view.ViewResult;
import codesquad.webserver.dispatcher.view.resolver.ViewResolver;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.httpresponse.HttpResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DispatcherServlet {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private final HandlerMapping handlerMapping;
    private final HandlerAdapter handlerAdapter;
    private final ViewResolver viewResolver;

    @Autowired
    public DispatcherServlet(HandlerMapping handlerMapping, HandlerAdapter handlerAdapter, ViewResolver viewResolver) {
        this.handlerMapping = handlerMapping;
        this.handlerAdapter = handlerAdapter;
        this.viewResolver = viewResolver;
    }

    public HttpResponse service(HttpRequest request) {
        try {
            Object handler = handlerMapping.getHandler(request);
            ModelAndView mv = handlerAdapter.handle(request, handler);

            View view = viewResolver.resolveView(mv);
            ViewResult viewResult = view.render(mv.getModel());

            return createHttpResponse(viewResult);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private HttpResponse createHttpResponse(ViewResult viewResult) {
        HttpResponseBuilder builder = HttpResponseBuilder.ok()
                .statusCode(viewResult.getStatusCode())
                .body(viewResult.getBody());

        for (HttpResponse.Header header : viewResult.getHeaders()) {
            builder.header(header.getName(), header.getValue());
        }

        viewResult.getCookies().forEach(builder::cookie);

        return builder.build();
    }

    private HttpResponse handleException(Exception e) {
        logger.error("Error occurred: ", e);
        ModelAndView errorMv = new ModelAndView(ViewName.EXCEPTION_VIEW);
        errorMv.addAttribute(ModelKey.ERROR_MESSAGE, e.getMessage());
        View errorView = viewResolver.resolveView(errorMv);
        ViewResult errorViewResult = errorView.render(errorMv.getModel());

        return createHttpResponse(errorViewResult);
    }
}