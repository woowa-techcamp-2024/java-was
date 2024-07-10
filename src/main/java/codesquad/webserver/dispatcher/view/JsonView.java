package codesquad.webserver.dispatcher.view;

import codesquad.webserver.httpresponse.HttpResponse;
import java.util.Map;

public class JsonView implements View {
    public JsonView(Map<String, Object> model) {
    }

    @Override
    public HttpResponse render(Map<String, ?> model) {
        return null;
    }
}
