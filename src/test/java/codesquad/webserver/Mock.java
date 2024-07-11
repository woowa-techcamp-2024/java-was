package codesquad.webserver;

import codesquad.webserver.http.HttpBody;
import codesquad.webserver.http.HttpHeader;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpRequestStartLine;

public class Mock {
    public static HttpRequest makeHttpRequest(){
        HttpRequestStartLine startLine = new HttpRequestStartLine("GET /index HTTP1.1");
        HttpHeader httpHeader = new HttpHeader();
        HttpBody httpBody = null;
        return new HttpRequest(startLine, httpHeader, httpBody);
    }

    public static HttpRequest makeHttpRequestWithCookie(String sid){
        HttpRequestStartLine startLine = new HttpRequestStartLine("GET /index HTTP1.1");
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.setValue("Cookie", "sid="+sid);
        HttpBody httpBody = null;
        return new HttpRequest(startLine, httpHeader, httpBody);
    }
}
