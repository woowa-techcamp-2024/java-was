package codesquad.webserver.parser;

import codesquad.webserver.parser.enums.HttpMethod;

public class RequestLine {
    private HttpMethod method;
    private String path;
    private String fullPath;
    private String httpVersion;

    public RequestLine() {

    }

    public RequestLine(HttpMethod method, String path, String fullPath, String httpVersion) {
        this.method = method;
        this.path = path;
        this.fullPath = fullPath;
        this.httpVersion = httpVersion;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getFullPath() {
        return fullPath;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    @Override
    public String toString() {
        return "RequestLine{" +
                "method=" + method +
                ", path='" + path + '\'' +
                ", fullPath='" + fullPath + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                '}';
    }
}
