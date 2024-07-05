package codesquad.webserver.http;

public class HttpResponseLine {

    private HttpVersion version;
    private HttpStatus status;

    public HttpResponseLine(HttpVersion version, HttpStatus status) {
        this.version = version;
        this.status = status;
    }

    public HttpVersion getVersion() {
        return version;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
