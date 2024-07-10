package codesquad.http;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HttpRequest {

    private final String uri;
    private final String method;
    private final String httpVersion;
    private final QueryParameters parameters;
    private final HttpHeaders headers;
    private String body;

    public HttpRequest(String uri, String method, String httpVersion, QueryParameters parameters, HttpHeaders headers, String body) {
        this.uri = uri;
        this.method = method;
        this.httpVersion = httpVersion;
        this.parameters = parameters;
        this.headers = headers;
        this.body = body;
    }

    public String uri() {
        return this.uri;
    }

    public String method() {
        return this.method;
    }

    public String httpVersion() {
        return this.httpVersion;
    }

    public HttpHeaders headers() {
        return Objects.requireNonNullElseGet(this.headers, HttpHeaders::empty);
    }

    public List<String> headerValues(String headerName) {
        return headers.getValues(headerName);
    }

    public Optional<String> body() {
        return this.body == null || this.body.isEmpty() ? Optional.empty() : Optional.of(this.body);
    }

    public Optional<String> firstParameterValue(String parameterName) {
        return parameters.getFirstValue(parameterName);
    }

    public Optional<String> firstHeaderValue(String headerName) {
        return headers.getFirstValue(headerName);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCookie(String cookieName) {
        return headers.getCookie(cookieName);
    }

    @Override
    public String toString() {
        return """
                HttpRequest{
                \turi='%s',
                \tmethod='%s',
                \thttpVersion='%s',
                \tparameters=%s,
                \theaders='%s',
                \tbody='%s'
                }
                """.formatted(uri, method, httpVersion, parameters, headers, body().orElse(""));
    }
}
