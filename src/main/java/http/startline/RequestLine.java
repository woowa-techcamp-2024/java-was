package http.startline;

import http.HttpMethod;

public class RequestLine extends StartLine {
    private final HttpMethod method;
    private final UrlPath urlPath;

    private RequestLine(HttpMethod method, UrlPath urlPath, String version) {
        super(version);
        this.method = method;
        this.urlPath = urlPath;
    }

    public static RequestLine fromString(String requestLine) {
        String[] split = requestLine.split(" ");
        if(split.length != 3) {
            throw new IllegalArgumentException("Request Line 형식이 잘못되었습니다.");
        }

        HttpMethod method = HttpMethod.of(split[0]);
        String path = split[1];
        String version = split[2];
        return new RequestLine(method, UrlPath.of(path), version);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public UrlPath getUrlPath() {
        return urlPath;
    }

    @Override
    public String toString() {
        return method + " " + urlPath.getPath() + " " + getVersion();
    }

}
