package http;

import http.startline.ResponseLine;

public class HttpResponse extends Http{


    public HttpResponse(ResponseLine startLine, Header header, byte[] body) {
        super(startLine, header, body);
    }

    public static HttpResponse generateHttpResponse() {
        return new HttpResponse(new ResponseLine(), Header.emptyHeader(), new byte[0]);
    }
}
