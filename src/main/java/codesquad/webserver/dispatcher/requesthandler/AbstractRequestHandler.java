package codesquad.webserver.dispatcher.requesthandler;

import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.httpresponse.HttpResponseBuilder;

public abstract class AbstractRequestHandler implements RequestHandler {

    protected final FileReader fileReader;

    protected AbstractRequestHandler(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        switch (request.requestLine().method()) {
            case GET:
                return handleGet(request);
            case POST:
                return handlePost(request);
            case PUT:
                return handlePut(request);
            case DELETE:
                return handleDelete(request);
            default:
                return handleMethodNotAllowed();
        }
    }

    protected HttpResponse handleGet(HttpRequest request) {
        return handleMethodNotAllowed();
    }

    protected HttpResponse handlePost(HttpRequest request) {
        return handleMethodNotAllowed();
    }

    protected HttpResponse handlePut(HttpRequest request) {
        return handleMethodNotAllowed();
    }

    protected HttpResponse handleDelete(HttpRequest request) {
        return handleMethodNotAllowed();
    }

    protected HttpResponse handleMethodNotAllowed() {
        return HttpResponseBuilder.buildMethodErrorResponse();
    }
}
