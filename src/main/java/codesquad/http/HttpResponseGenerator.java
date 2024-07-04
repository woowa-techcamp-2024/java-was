package codesquad.http;

public class HttpResponseGenerator {

    public static final String CRLF = "\r\n";

    public HttpResponse sendOK(String body, String contentType) {
        return generate(StatusCode.OK, body, contentType);
    }

    public HttpResponse sendNotFound(String body, String contentType) {
        return generate(StatusCode.NOT_FOUND, body, contentType);
    }

    private HttpResponse generate(StatusCode statusCode, String body, String contentType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addValue("Content-Type", contentType);
        return new HttpResponse(statusCode, httpHeaders, body);
    }

    public byte[] generate(String body, String contentType) {
        String response = "HTTP/1.1 200 OK" + CRLF +
                "Content-Type: " + contentType + CRLF +
                CRLF +
                "Content-Length: " + body.getBytes().length;
        return response.getBytes();
    }

}
