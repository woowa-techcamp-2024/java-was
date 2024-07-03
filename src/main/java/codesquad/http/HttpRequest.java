package codesquad.http;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class HttpRequest {

    private final String uri;
    private final String method;
    private final HttpHeaders headers;
    private final String body;

    protected HttpRequest(String uri, String method, HttpHeaders headers, String body) {
        this.uri = uri;
        this.method = method;
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequest fromText(String request) {
        if (request == null || request.isEmpty()) {
            //TODO 커스텀 Exception을 만들어 사용하는 것은 어떨지 생각해보기
            throw new IllegalArgumentException("[ERROR] HTTP request의 내용이 없습니다.");
        }

        String[] lines = request.split(System.lineSeparator());
        String[] requestLine = lines[0].split(" ");
        String method = requestLine[0];
        String uri = requestLine[1];
        String headers = Arrays.stream(lines)
                .skip(1)
                .collect(Collectors.joining(System.lineSeparator()));
        HttpHeaders httpHeaders = HttpHeaders.fromText(headers);
        return new HttpRequest(uri, method, httpHeaders, null);
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
