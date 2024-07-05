package codesquad.webserver.http;

public class HttpRequest {
    private final HttpRequestStartLine startLine;
    private final HttpHeader httpHeader;

    public HttpRequest(HttpRequestStartLine startLine, HttpHeader httpHeader) {
        this.startLine = startLine;
        this.httpHeader = httpHeader;
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

    @Override
    public String toString() {
        return "HttpRequest{" +
                startLine +
                ", " + httpHeader +
                '}';
    }
}
