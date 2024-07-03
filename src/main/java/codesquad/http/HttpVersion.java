package codesquad.http;

import java.util.Arrays;

public enum HttpVersion {

    HTTP1_1("HTTP/1.1");

    private final String name;

    HttpVersion(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // FIXME: HashMap 캐시하는 로직으로 변경
    public static HttpVersion of(String version) {
        return Arrays.stream(values())
                .filter(httpVersion -> httpVersion.name.equals(version))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 HttpVersion 입니다."));
    }

}
