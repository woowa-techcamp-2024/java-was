package codesquad.webserver.dispatcher.view;

import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.httpresponse.HttpResponseBuilder;
import java.util.Map;


public class ExceptionView implements View {
    public ExceptionView(String viewName, Map<String, Object> model) {
    }

    @Override
    public HttpResponse render(Map<String, ?> model) {
        Integer statusCode = (Integer)model.get("statusCode");
        if (statusCode == null) {
            throw new IllegalStateException("statusCode cannot be null");
        }

        if (statusCode == 405) {
            return HttpResponseBuilder.buildMethodErrorResponse();
        }
        return HttpResponseBuilder.buildNotFoundResponse();
    }
}
