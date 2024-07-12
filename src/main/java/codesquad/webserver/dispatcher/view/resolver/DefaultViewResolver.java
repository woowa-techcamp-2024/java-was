package codesquad.webserver.dispatcher.view.resolver;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.annotation.Component;
import codesquad.webserver.dispatcher.view.ExceptionView;
import codesquad.webserver.dispatcher.view.JsonView;
import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.dispatcher.view.RedirectView;
import codesquad.webserver.dispatcher.view.TemplateView;
import codesquad.webserver.dispatcher.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DefaultViewResolver implements ViewResolver {

    private static final Logger log = LoggerFactory.getLogger(DefaultViewResolver.class);

    private final TemplateView templateView;
    private final ExceptionView exceptionView;
    private final JsonView jsonView;
    private final RedirectView redirectView;

    @Autowired
    public DefaultViewResolver(TemplateView templateView, ExceptionView exceptionView, JsonView jsonView,
                               RedirectView redirectView) {
        this.templateView = templateView;
        this.exceptionView = exceptionView;
        this.jsonView = jsonView;
        this.redirectView = redirectView;
    }

    @Override
    public View resolveView(ModelAndView modelAndView) {
        switch (modelAndView.getViewName()) {
            case TEMPLATE_VIEW -> {
                return templateView;
            }
            case REDIRECT_VIEW -> {
                return redirectView;
            }
            case JSON_VIEW -> {
                return jsonView;
            }
            case EXCEPTION_VIEW -> {
                return exceptionView;
            }
        }
        throw new IllegalArgumentException("Unknown view name: " + modelAndView.getViewName());
    }
}
