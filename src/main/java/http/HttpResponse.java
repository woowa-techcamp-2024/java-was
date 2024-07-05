package http;

import http.startline.ResponseLine;
import java.io.IOException;

public class HttpResponse extends Http{


    public HttpResponse(ResponseLine startLine, Header header, byte[] body) {
        super(startLine, header, body);
    }

    public static HttpResponse generateHttpResponse() throws IOException {
        return new HttpResponse(new ResponseLine(), Header.emptyHeader(), new byte[0]);
    }
}
