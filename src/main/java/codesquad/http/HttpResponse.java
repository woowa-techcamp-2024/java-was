package codesquad.http;

public class HttpResponse {

    private final StatusCode statusCode;
    private final HttpHeaders headers;
    private final String body;

    public HttpResponse(StatusCode statusCode, HttpHeaders headers, String body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

}
