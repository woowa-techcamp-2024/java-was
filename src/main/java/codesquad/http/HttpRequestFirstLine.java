package codesquad.http;

public class HttpRequestFirstLine {

    private final HttpVersion version;
    private final HttpMethod method;
    private final String path;

    public HttpRequestFirstLine(HttpVersion version, HttpMethod method, String path) {
        this.version = version;
        this.method = method;
        this.path = path;
    }

    public HttpVersion getVersion() {
        return version;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return method + " " + path + " " + version + "\n";
    }
}
