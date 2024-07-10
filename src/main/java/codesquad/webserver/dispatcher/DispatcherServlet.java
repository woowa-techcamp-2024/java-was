package codesquad.webserver.dispatcher;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.annotation.Component;
import codesquad.webserver.dispatcher.handler.adater.HandlerAdapter;
import codesquad.webserver.dispatcher.handler.mapping.HandlerMapping;
import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.dispatcher.view.View;
import codesquad.webserver.dispatcher.view.resolver.ViewResolver;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
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
        Object handler = handlerMapping.getHandler(request);
        ModelAndView mv = handlerAdapter.handle(request, handler);

        View view = viewResolver.resolveView(mv, request);
        return view.render(mv.getModel());
    }
}
