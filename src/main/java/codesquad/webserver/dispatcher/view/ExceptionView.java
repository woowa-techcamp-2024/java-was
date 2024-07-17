package codesquad.webserver.dispatcher.view;

import codesquad.webserver.annotation.Component;
import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.httpresponse.HttpResponseBuilder;
import java.util.Map;
import java.util.Optional;

@Component
public class ExceptionView implements View {

    @Override
    public ViewResult render(Map<String, ?> model) {
        Map<String, Object> objectMap = (Map<String, Object>) model;
        int statusCode = (int) Optional.ofNullable(objectMap.get("statusCode")).orElse(500);
        HttpResponse response = HttpResponseBuilder.serverError().build();
        switch (statusCode) {
            case 405 -> response = HttpResponseBuilder.buildForbiddenFromFile();
            case 404 -> response = HttpResponseBuilder.buildNotFoundFromFile();
            case 403 -> response = HttpResponseBuilder.buildForbiddenFromFile();
        }

        return new ViewResult(
                response.getBody(),
                response.getCookies(),
                response.getHeaders(),
                response.getStatusCode()
        );
    }
}