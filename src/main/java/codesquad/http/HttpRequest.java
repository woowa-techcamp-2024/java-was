package codesquad.http;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HttpRequest {

    private final String uri;
    private final String method;
    private final HttpHeaders headers;
    private final String body;

    public HttpRequest(String uri, String method, HttpHeaders headers, String body) {
        this.uri = uri;
        this.method = method;
        this.headers = headers;
        this.body = body;
    }

    public String uri() {
        return this.uri;
    }

    public String method() {
        return this.method;
    }

    public HttpHeaders headers() {
        return Objects.requireNonNullElseGet(this.headers, HttpHeaders::empty);
    }

    public List<String> headerValues(String headerName) {
        return headers.getValues(headerName);
    }

    public Optional<String> body() {
        return this.body == null || this.body.isEmpty() ? Optional.empty()
                : Optional.of(this.body);
    }

    @Override
    public String toString() {
        return """
                HttpRequest{
                \turi='%s',
                \tmethod='%s',
                \theaders='%s',
                \tbody='%s'
                }
                """.formatted(this.uri, this.method, headers, body().orElse(""));
    }

}
