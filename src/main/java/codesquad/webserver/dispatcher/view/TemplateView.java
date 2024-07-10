package codesquad.webserver.dispatcher.view;

import codesquad.webserver.httpresponse.HttpResponse;
import java.util.Map;


public class TemplateView implements View {
    public TemplateView(String viewName, Map<String, Object> model) {
    }

    @Override
    public HttpResponse render(Map<String, ?> model) {
        return null;
    }
}
