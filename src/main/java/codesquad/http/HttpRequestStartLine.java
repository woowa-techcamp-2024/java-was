package codesquad.http;

public class HttpRequestStartLine {
    private final HttpMethod method;
    private final String path;
    private String queryString;

    public HttpRequestStartLine(String line){
        String[] tokens = line.split(" ");
        this.method = HttpMethod.valueOf(tokens[0]);
        String[] url = tokens[1].split("\\?");
        this.path = url[0];
        if(url.length == 2){
            this.queryString = url[1];
        }
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQueryString() {
        return queryString;
    }

    @Override
    public String toString() {
        return "HttpRequestStartLine{" +
                "method=" + method +
                ", path='" + path + '\'' +
                ", queryString='" + queryString + '\'' +
                '}';
    }
}
