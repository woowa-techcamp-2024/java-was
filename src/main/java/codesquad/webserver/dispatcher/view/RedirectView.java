package codesquad.webserver.dispatcher.view;

import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.httpresponse.HttpResponseBuilder;
import java.util.Map;

public class RedirectView implements View {

    private final String url;

    public RedirectView(String url) {
        this.url = url;
    }

    @Override
    public HttpResponse render(Map<String, ?> model) {
        return HttpResponseBuilder.buildRedirectResponse(url);
    }
}
