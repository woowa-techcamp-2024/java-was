package codesquad.http.parser;

import codesquad.format.Parser;
import codesquad.http.HttpHeaders;
import codesquad.http.HttpRequest;
import codesquad.http.QueryParameters;

import java.util.Arrays;
import java.util.stream.Collectors;

public class HttpRequestParser implements Parser<HttpRequest> {

    private final HttpHeadersParser headersParser;
    private final QueryParametersParser queryParametersParser;

    public HttpRequestParser(HttpHeadersParser headersParser, QueryParametersParser queryParametersParser) {
        this.headersParser = headersParser;
        this.queryParametersParser = queryParametersParser;
    }

    @Override
    public HttpRequest parse(String requestText) {
        if (requestText == null || requestText.isEmpty()) {
            //TODO 커스텀 Exception을 만들어 사용하는 것은 어떨지 생각해보기
            throw new IllegalArgumentException("[ERROR] HTTP request의 내용이 없습니다.");
        }

        String[] lines = requestText.split("\r\n");
        String[] requestLine = lines[0].split(" ");
        String method = requestLine[0];
        String uri = requestLine[1];
        String httpVersion = requestLine[2];

        String headers = Arrays.stream(lines)
                .skip(1)
                .collect(Collectors.joining("\r\n"));
        HttpHeaders httpHeaders = headersParser.parse(headers);
        QueryParameters queryParameters = queryParametersParser.parse(uri);
        if (queryParameters.isExists()) {
            uri = uri.split("\\?")[0];
        }
        return new HttpRequest(uri, method, httpVersion, queryParameters, httpHeaders, null);
    }

}
