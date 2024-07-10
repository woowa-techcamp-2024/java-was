package codesquad.webserver.http;

import static codesquad.utils.string.StringUtils.CRLF;
import static org.assertj.core.api.Assertions.assertThat;

import codesquad.webserver.parser.HttpRequestMapper;
import codesquad.webserver.parser.HttpRequestParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRequestMapperTest {

    HttpRequestMapper httpRequestMapper = new HttpRequestMapper(new HttpRequestParser());

    @Test
    @DisplayName("[Success] Request Body가 없는 요청")
    void parserTest() throws IOException {
        String request = "GET /index.html HTTP/1.1" + CRLF +
                "Host: localhost:8080" + CRLF +
                "User-Agent: Mozilla/5.0" + CRLF +
                CRLF;

        BufferedReader bufferedReader = new BufferedReader(new StringReader(request));
        HttpRequest httpRequest = httpRequestMapper.mapFrom(bufferedReader);

        assertFirstLine(httpRequest);
        assertHeaders(httpRequest);
    }

    private static void assertFirstLine(HttpRequest httpRequest) {
        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getPath().getDefaultPath()).isEqualTo("/index.html");
        assertThat(httpRequest.getVersion()).isEqualTo(HttpVersion.HTTP1_1);
    }

    private void assertHeaders(HttpRequest httpRequest) {
        assertThat(httpRequest.getHeaderValue("Host"))
                .isPresent()
                .get().isEqualTo("localhost:8080");
        assertThat(httpRequest.getHeaderValue("User-Agent"))
                .isPresent()
                .get().isEqualTo("Mozilla/5.0");
    }

    @Test
    @DisplayName("[Success] Request Body가 있는 요청")
    void requestBodyTest() throws IOException {
        String request = """
                POST /index.html HTTP/1.1
                Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
                Accept-Encoding: gzip, deflate, br, zstd
                Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7
                Cache-Control: max-age=0
                Connection: keep-alive
                Content-Length: 104
                Content-Type: application/x-www-form-urlencoded
                Host: localhost:8080
                Origin: http://localhost:8080
                Referer: http://localhost:8080/registration
                Sec-Fetch-Dest: document
                Sec-Fetch-Mode: navigate
                Sec-Fetch-Site: same-origin
                Sec-Fetch-User: ?1
                Upgrade-Insecure-Requests: 1
                User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36
                sec-ch-ua: "Not/A)Brand";v="8", "Chromium";v="126", "Google Chrome";v="126"
                sec-ch-ua-mobile: ?0
                sec-ch-ua-platform: "macOS"
                """
                + CRLF
                + "userId=ThisIsId%21%40%23&name=ThisIsNickname%25%5E&password=qlalfqjsgh&email=emailemailemail%40gmail.com";
        // given
        BufferedReader bufferedReader = new BufferedReader(new StringReader(request));
        HttpRequest httpRequest = httpRequestMapper.mapFrom(bufferedReader);
        System.out.println("httpRequest = " + httpRequest);
        // when
        // then
        assertThat(httpRequest.getBodyParamValue("userId")).isEqualTo("ThisIsId!@#");
        assertThat(httpRequest.getBodyParamValue("name")).isEqualTo("ThisIsNickname%^");
        assertThat(httpRequest.getBodyParamValue("password")).isEqualTo("qlalfqjsgh");
        assertThat(httpRequest.getBodyParamValue("email")).isEqualTo("emailemailemail@gmail.com");
    }

}