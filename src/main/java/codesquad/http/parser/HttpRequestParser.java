package codesquad.http.parser;

import codesquad.http.HttpHeaders;
import codesquad.http.HttpRequest;
import codesquad.http.QueryParameters;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class HttpRequestParser {

    private static HttpRequestParser instance;

    private final HttpHeadersParser headersParser;
    private final QueryParametersParser queryParametersParser;

    private HttpRequestParser(HttpHeadersParser headersParser, QueryParametersParser queryParametersParser) {
        this.headersParser = headersParser;
        this.queryParametersParser = queryParametersParser;
    }

    public static HttpRequestParser getInstance(HttpHeadersParser headersParser, QueryParametersParser queryParametersParser) {
        if (instance == null) {
            instance = new HttpRequestParser(headersParser, queryParametersParser);
        }
        return instance;
    }

    public HttpRequest parse(String requestText) {
        // TODO 예외 처리 + optional whitespace 처리
        if (requestText == null || requestText.isEmpty()) {
            return null;
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

        QueryParameters queryParameters = null;
        if (uri.contains("?")) {
            String[] uriQueryString = uri.split("\\?");
            queryParameters = queryParametersParser.parse(uriQueryString[1]);
            uri = uriQueryString[0];
        }
        return new HttpRequest(uri, method, httpVersion, queryParameters, httpHeaders, null);
    }
}
