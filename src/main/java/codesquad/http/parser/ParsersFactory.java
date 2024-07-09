package codesquad.http.parser;

public class ParsersFactory {

    private static final HttpHeadersParser httpHeadersParser = new HttpHeadersParser();
    private static final QueryParametersParser queryParametersParser = new QueryParametersParser();
    private static final HttpRequestParser httpRequestParser = new HttpRequestParser(httpHeadersParser, queryParametersParser);

    public static HttpRequestParser getHttpRequestParser() {
        return httpRequestParser;
    }

    public static QueryParametersParser getQueryParametersParser() {
        return queryParametersParser;
    }

}
