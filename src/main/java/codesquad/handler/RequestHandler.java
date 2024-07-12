package codesquad.handler;

import codesquad.error.HttpStatusException;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpResponseGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RequestHandler {

    private final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    protected final HttpResponseGenerator responseGenerator;

    protected RequestHandler() {
        this.responseGenerator = HttpResponseGenerator.getInstance();
    }

    public HttpResponse handle(HttpRequest httpRequest) {
        try {
            if (httpRequest.method().equals("GET")) {
                return handleGet(httpRequest);
            }
            if (httpRequest.method().equals("POST")) {
                return handlePost(httpRequest);
            }
            return responseGenerator.sendMethodNotAllowed(httpRequest);
        } catch (RuntimeException e) {
            log.warn(e.getMessage(), e);
            return handleError(httpRequest, e);
        }
    }

    protected HttpResponse handleGet(HttpRequest httpRequest) {
        return responseGenerator.sendMethodNotAllowed(httpRequest);
    }

    protected HttpResponse handlePost(HttpRequest httpRequest) {
        return responseGenerator.sendMethodNotAllowed(httpRequest);
    }

    private HttpResponse handleError(HttpRequest httpRequest, RuntimeException e) {
        if (e instanceof HttpStatusException httpStatusException) {
            String message = String.format("<h1>%s</h1><p>%s</p>", httpStatusException.getStatusMessage(), e.getMessage());
            return responseGenerator.sendError(httpRequest, httpStatusException.statusCode(), message);
        }
        return responseGenerator.sendInternalServerError(httpRequest);
    }
}
