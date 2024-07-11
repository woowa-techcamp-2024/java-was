package codesquad.webserver.dispatcher.view;

import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.httpresponse.HttpResponseBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedirectView implements View {

    private final String url;
    private final Map<String, Object> model;

    public RedirectView(String url, Map<String, Object> model) {
        this.url = url;
        this.model = model;
    }

    @Override
    public HttpResponse render(Map<String, ?> model) {
        Map<String, List<String>> headers = (Map<String, List<String>>) model.get("headers");
        return headers != null ? HttpResponseBuilder.buildRedirectResponse(url, headers) : HttpResponseBuilder.buildRedirectResponse(url);
    }
}
