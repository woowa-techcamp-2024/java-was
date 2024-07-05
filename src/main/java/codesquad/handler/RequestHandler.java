package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpResponseGenerator;

public abstract class RequestHandler {

    protected final HttpResponseGenerator responseGenerator;

    protected RequestHandler() {
        this.responseGenerator = new HttpResponseGenerator();
    }

    public abstract HttpResponse handle(HttpRequest httpRequest);

}
