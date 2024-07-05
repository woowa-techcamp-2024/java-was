package codesquad.webserver.http;

public class HttpRequestLine {

    private final HttpMethod method;
    private final HttpPath path;
    private final HttpVersion version;

    public HttpRequestLine(HttpMethod method, HttpPath path, HttpVersion version) {
        this.method = method;
        this.path = path;
        this.version = version;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public HttpPath getPath() {
        return path;
    }

    public HttpVersion getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "{" + method + " " + path + " " + version + "}";
    }
}
