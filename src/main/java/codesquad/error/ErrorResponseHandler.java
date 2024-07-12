package codesquad.error;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpResponseGenerator;
import codesquad.http.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ErrorResponseHandler {

    private static ErrorResponseHandler instance;

    private final Logger log = LoggerFactory.getLogger(ErrorResponseHandler.class);

    private final HttpResponseGenerator responseGenerator = HttpResponseGenerator.getInstance();

    private ErrorResponseHandler() {
    }

    public static ErrorResponseHandler getInstance() {
        if (instance == null) {
            instance = new ErrorResponseHandler();
        }
        return instance;
    }

    public HttpResponse handle(RuntimeException e, HttpRequest httpRequest) {
        if (e instanceof HttpStatusException httpStatusException) {
            log.debug(httpStatusException.getMessage(), httpStatusException);
            StatusCode errorStatusCode = httpStatusException.statusCode();
            return switch (errorStatusCode) {
                case NOT_FOUND -> responseGenerator.sendNotFound(httpRequest);
                default -> responseGenerator.sendBadRequest(httpRequest);
            };
        }
        return responseGenerator.sendInternalServerError(httpRequest);
    }

}
