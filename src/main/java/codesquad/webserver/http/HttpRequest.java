package codesquad.webserver.http;

import java.util.Map;

public class HttpRequest {
    private final HttpRequestStartLine startLine;
    private final HttpHeader httpHeader;
    private final HttpBody httpBody;

    public HttpRequest(HttpRequestStartLine startLine, HttpHeader httpHeader, HttpBody httpBody) {
        this.startLine = startLine;
        this.httpHeader = httpHeader;
        this.httpBody = httpBody;
    }

    public HttpMethod getHttpMethod() {
        return startLine.getMethod();
    }
    public String getPath(){
        return startLine.getPath();
    }
    public String getQueryString(){
        return startLine.getQueryString();
    }

    public HttpHeader getHttpHeader() {
        return httpHeader;
    }

    public Map<String, Object> getHttpBody(){return httpBody.getBody();}

    @Override
    public String toString() {
        return "HttpRequest{" +
                startLine +
                ", " + httpHeader +
                ", " + httpBody +
                '}';
    }
}
