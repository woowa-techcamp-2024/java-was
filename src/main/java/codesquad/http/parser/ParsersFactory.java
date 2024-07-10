package codesquad.http.parser;

public class ParsersFactory {

    private static final HttpHeadersParser httpHeadersParser = HttpHeadersParser.getInstance();
    private static final QueryParametersParser queryParametersParser = QueryParametersParser.getInstance();
    private static final HttpRequestParser httpRequestParser = HttpRequestParser.getInstance(httpHeadersParser, queryParametersParser);

    public static HttpRequestParser getHttpRequestParser() {
        return httpRequestParser;
    }

    public static QueryParametersParser getQueryParametersParser() {
        return queryParametersParser;
    }

    public static HttpHeadersParser getHeadersParser() {
        return httpHeadersParser;
    }

}
