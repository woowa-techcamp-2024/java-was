package codesquad.http;

public final class HttpResponseGenerator {

    private static HttpResponseGenerator instance;

    private HttpResponseGenerator() {
    }

    public static HttpResponseGenerator getInstance() {
        if (instance == null) {
            instance = new HttpResponseGenerator();
        }
        return instance;
    }

    public HttpResponse sendOK(byte[] body, MediaType mediaType, HttpRequest httpRequest) {
        return generate(StatusCode.OK, body, mediaType, httpRequest);
    }

    public HttpResponse sendNotFound(HttpRequest httpRequest) {
        return generate(StatusCode.NOT_FOUND, "<h1>404 Not Found</h1>".getBytes(), MediaType.TEXT_HTML, httpRequest);
    }

    public HttpResponse sendRedirect(HttpRequest httpRequest, String location) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addValue(HttpHeaders.LOCATION, location);
        return generate(StatusCode.SEE_OTHER, null, httpHeaders, httpRequest);
    }

    public HttpResponse sendBadRequest(HttpRequest httpRequest) {
        return generate(StatusCode.BAD_REQUEST, "<h1>400 Bad Request</h1>".getBytes(), MediaType.TEXT_HTML, httpRequest);
    }

    public HttpResponse sendMethodNotAllowed(HttpRequest httpRequest) {
        return generate(StatusCode.METHOD_NOT_ALLOWED, "<h1>405 Method Not Allowed</h1>".getBytes(), MediaType.TEXT_HTML, httpRequest);
    }

    public HttpResponse sendInternalServerError(HttpRequest httpRequest) {
        return generate(StatusCode.INTERNAL_SERVER_ERROR, "<h1>500 Internal Server Error</h1>".getBytes(), MediaType.TEXT_HTML, httpRequest);
    }

    private HttpResponse generate(StatusCode statusCode, byte[] body, MediaType mediaType, HttpRequest httpRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addValue(HttpHeaders.CONTENT_TYPE, mediaType.getValue());
        httpHeaders.addValue(HttpHeaders.CONTENT_LENGTH, String.valueOf(body.length));
        return generate(statusCode, body, httpHeaders, httpRequest);
    }

    private HttpResponse generate(StatusCode statusCode, byte[] body, HttpHeaders httpHeaders, HttpRequest httpRequest) {
        return new HttpResponse(statusCode, httpRequest.httpVersion(), httpHeaders, body);
    }
}
