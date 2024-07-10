package codesquad.webserver.dispatcher.view.resolver;

import codesquad.webserver.annotation.Component;
import codesquad.webserver.dispatcher.view.JsonView;
import codesquad.webserver.dispatcher.view.ExceptionView;
import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.dispatcher.view.RedirectView;
import codesquad.webserver.dispatcher.view.TemplateView;
import codesquad.webserver.dispatcher.view.View;
import codesquad.webserver.httprequest.HttpRequest;
import java.util.Map;

@Component
public class DefaultViewResolver implements ViewResolver {
    @Override
    public View resolveView(ModelAndView modelAndView, HttpRequest request) {
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();

        Map<String, String> headers = request.headers();

        if (viewName.startsWith("redirect:")) {
            return new RedirectView(viewName.substring(9));
        } else if (viewName.equals("jsonView")) {
            return new JsonView(model);
        } else if (viewName.equals("templateView")){
            return new TemplateView(viewName, model);
        } else {
            return new ExceptionView(viewName, model);
        }
    }
}
