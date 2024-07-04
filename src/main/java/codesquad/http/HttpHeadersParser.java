package codesquad.http;

import java.util.Arrays;

public class HttpHeadersParser {

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
