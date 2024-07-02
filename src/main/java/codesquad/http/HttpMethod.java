package codesquad.http;

public enum HttpMethod {
    GET,
    POST,
    ;

    public boolean isPost(){
        return this == POST;
    }

    public boolean isGet() {
        return this == GET;
    }
}
