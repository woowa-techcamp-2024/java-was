package codesquad.webserver.dispatcher.view;

import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.session.cookie.HttpCookie;
import java.util.List;

public class ViewResult {
    private final byte[] body;
    private final List<HttpCookie> cookies;
    private final List<HttpResponse.Header> headers;
    private final int statusCode;

    public ViewResult(byte[] body, List<HttpCookie> cookies, List<HttpResponse.Header> headers, int statusCode) {
        this.body = body;
        this.cookies = cookies;
        this.headers = headers;
        this.statusCode = statusCode;
    }

    // Getters
    public byte[] getBody() {
        return body;
    }

    public List<HttpCookie> getCookies() {
        return cookies;
    }

    public List<HttpResponse.Header> getHeaders() {
        return headers;
    }

    public int getStatusCode() {
        return statusCode;
    }
}