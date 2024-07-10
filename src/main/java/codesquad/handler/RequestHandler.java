package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpResponseGenerator;

public abstract class RequestHandler {

    protected final HttpResponseGenerator responseGenerator;

    protected RequestHandler() {
        this.responseGenerator = new HttpResponseGenerator();
    }

    public HttpResponse handle(HttpRequest httpRequest) {
        if (httpRequest.method().equals("GET")) {
            return handleGet(httpRequest);
        }
        if (httpRequest.method().equals("POST")) {
            return handlePost(httpRequest);
        }
        return responseGenerator.sendMethodNotAllowed(httpRequest);
    }

    protected HttpResponse handleGet(HttpRequest httpRequest) {
        return responseGenerator.sendMethodNotAllowed(httpRequest);
    }

    protected HttpResponse handlePost(HttpRequest httpRequest) {
        return responseGenerator.sendMethodNotAllowed(httpRequest);
    }

}
