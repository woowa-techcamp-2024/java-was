package codesquad.http.parser;

import codesquad.http.HttpHeaders;

import java.util.Arrays;

public final class HttpHeadersParser implements Parser<HttpHeaders> {

    private static HttpHeadersParser instance;

    private HttpHeadersParser() {
    }

    public static HttpHeadersParser getInstance() {
        if (instance == null) {
            instance = new HttpHeadersParser();
        }
        return instance;
    }

    @Override
    public HttpHeaders parse(String headersText) {
        if (headersText == null || headersText.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] HTTP header의 내용이 없습니다.");
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        String[] lines = headersText.split("\r\n");
        Arrays.stream(lines)
                .map(line -> line.split(": ", 2))
                .forEach(httpHeaders::putHeader);
        return httpHeaders;
    }

}
