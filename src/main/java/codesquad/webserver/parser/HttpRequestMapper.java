package codesquad.webserver.parser;

import codesquad.webserver.http.HttpHeaders;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpRequestBody;
import codesquad.webserver.http.HttpRequestLine;
import java.io.BufferedReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestMapper {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestMapper.class);

    private final HttpRequestParser httpRequestParser;

    public HttpRequestMapper(HttpRequestParser httpRequestParser) {
        this.httpRequestParser = httpRequestParser;
    }

    public HttpRequest mapFrom(BufferedReader bufferedReader) throws IOException {
        HttpRequestLine httpRequestLine = httpRequestParser.parseHttpRequestFirstLine(bufferedReader);
        HttpHeaders httpHeaders = httpRequestParser.parseHeaders(bufferedReader);
        HttpRequestBody httpRequestBody = httpRequestParser.parseBody(bufferedReader, httpHeaders.getContentLength());

        logger.debug("http request line: {}", httpRequestLine);
        logger.debug("http request headers: {}", httpHeaders);
        logger.debug("http request body: {}", httpRequestBody);

        return new HttpRequest(httpRequestLine, httpHeaders, httpRequestBody);
    }

}
