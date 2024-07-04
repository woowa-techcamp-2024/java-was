package codesquad.http;

import java.util.Arrays;
import java.util.stream.Collectors;

public class HttpRequestParser {

    private final HttpHeadersParser headersParser;

    public HttpRequestParser(HttpHeadersParser headersParser) {
        this.headersParser = headersParser;
    }

    public HttpRequest parse(String request) {
        if (request == null || request.isEmpty()) {
            //TODO 커스텀 Exception을 만들어 사용하는 것은 어떨지 생각해보기
            throw new IllegalArgumentException("[ERROR] HTTP request의 내용이 없습니다.");
        }

        String[] lines = request.split("\r\n");
        String[] requestLine = lines[0].split(" ");
        String method = requestLine[0];
        String uri = requestLine[1];
        String headers = Arrays.stream(lines)
                .skip(1)
                .collect(Collectors.joining("\r\n"));
        HttpHeaders httpHeaders = headersParser.parse(headers);
        return new HttpRequest(uri, method, httpHeaders, null);
    }

}
