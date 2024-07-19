package codesquad.webserver.http;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.servlet.execption.GlobalExceptionHandler;
import codesquad.servlet.handler.resource.StaticResourceReader;
import codesquad.webserver.parser.HttpRequestMapper;
import codesquad.webserver.parser.HttpRequestParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRequestMapperTest {

    StaticResourceReader staticResourceReader = StaticResourceReader.getInstance();
    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler(staticResourceReader);
    HttpRequestMapper httpRequestMapper = new HttpRequestMapper(new HttpRequestParser(), globalExceptionHandler);

    @Test
    @DisplayName("[Success] Request Body가 없는 요청")
    void parserTest() throws IOException {
        String request = "GET /index.html HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));

        HttpResponse httpResponse = HttpResponse.ok();
        HttpRequest httpRequest = httpRequestMapper.mapFrom(inputStream, httpResponse);

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
    @DisplayName("[Success] LF로 줄바꿈되어 있는 요청은 파싱을 성공한다")
    void lineFeedRequestBodyTest() throws IOException {
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
                User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36
                sec-ch-ua: "Not/A)Brand";v="8", "Chromium";v="126", "Google Chrome";v="126"
                sec-ch-ua-mobile: ?0
                sec-ch-ua-platform: "macOS"
                """
                + "\n"
                + "userId=ThisIsId%21%40%23&name=ThisIsNickname%25%5E&password=qlalfqjsgh&email=emailemailemail%40gmail.com";
        // given
        InputStream inputStream = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        HttpResponse httpResponse = HttpResponse.ok();
        HttpRequest httpRequest = httpRequestMapper.mapFrom(inputStream, httpResponse);
        System.out.println("httpRequest = " + httpRequest);
        // when
        // then
        assertThat(httpRequest.getBodyParamValue("userId")).isEqualTo("ThisIsId!@#");
        assertThat(httpRequest.getBodyParamValue("name")).isEqualTo("ThisIsNickname%^");
        assertThat(httpRequest.getBodyParamValue("password")).isEqualTo("qlalfqjsgh");
        assertThat(httpRequest.getBodyParamValue("email")).isEqualTo("emailemailemail@gmail.com");
    }

    @Test
    @DisplayName("[Success] CR로 줄바꿈되어 있는 요청은 파싱을 성공한다")
    void carriageReturnRequestBodyTest() throws IOException {
        String request =
                "POST /index.html HTTP/1.1\r"
                        + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r"
                        + "Accept-Encoding: gzip, deflate, br, zstd\r"
                        + "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\r"
                        + "Cache-Control: max-age=0\r"
                        + "Connection: keep-alive\r"
                        + "Content-Length: 104\r"
                        + "Content-Type: application/x-www-form-urlencoded\r"
                        + "Host: localhost:8080\r"
                        + "Origin: http://localhost:8080\r"
                        + "Referer: http://localhost:8080/registration\r"
                        + "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36\r"
                        + "sec-ch-ua: \"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";v=\"126\"\r"
                        + "sec-ch-ua-mobile: ?0\r"
                        + "sec-ch-ua-platform: \"macOS\r"
                        + "\r"
                        + "userId=ThisIsId%21%40%23&name=ThisIsNickname%25%5E&password=qlalfqjsgh&email=emailemailemail%40gmail.com";
        // given
        InputStream inputStream = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        HttpResponse httpResponse = HttpResponse.ok();
        HttpRequest httpRequest = httpRequestMapper.mapFrom(inputStream, httpResponse);
        System.out.println("httpRequest = " + httpRequest);
        // when
        // then
        assertThat(httpRequest.getBodyParamValue("userId")).isEqualTo("ThisIsId!@#");
        assertThat(httpRequest.getBodyParamValue("name")).isEqualTo("ThisIsNickname%^");
        assertThat(httpRequest.getBodyParamValue("password")).isEqualTo("qlalfqjsgh");
        assertThat(httpRequest.getBodyParamValue("email")).isEqualTo("emailemailemail@gmail.com");
    }

    @Test
    @DisplayName("[Success] CRLF로 줄바꿈되어 있는 요청은 파싱을 성공한다")
    void crlfRequestBodyTest() throws IOException {
        String request =
                "POST /index.html HTTP/1.1\r\n"
                        + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n"
                        + "Accept-Encoding: gzip, deflate, br, zstd\r\n"
                        + "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\r\n"
                        + "Cache-Control: max-age=0\r\n"
                        + "Connection: keep-alive\r\n"
                        + "Content-Length: 104\r\n"
                        + "Content-Type: application/x-www-form-urlencoded\r\n"
                        + "Host: localhost:8080\r\n"
                        + "Origin: http://localhost:8080\r\n"
                        + "Referer: http://localhost:8080/registration\r\n"
                        + "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36\r\n"
                        + "sec-ch-ua: \"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";v=\"126\"\r\n"
                        + "sec-ch-ua-mobile: ?0\r\n"
                        + "sec-ch-ua-platform: \"macOS\r\n"
                        + "\r\n"
                        + "userId=ThisIsId%21%40%23&name=ThisIsNickname%25%5E&password=qlalfqjsgh&email=emailemailemail%40gmail.com";
        // given
        InputStream inputStream = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        HttpResponse httpResponse = HttpResponse.ok();
        HttpRequest httpRequest = httpRequestMapper.mapFrom(inputStream, httpResponse);
        System.out.println("httpRequest = " + httpRequest);
        // when
        // then
        assertThat(httpRequest.getBodyParamValue("userId")).isEqualTo("ThisIsId!@#");
        assertThat(httpRequest.getBodyParamValue("name")).isEqualTo("ThisIsNickname%^");
        assertThat(httpRequest.getBodyParamValue("password")).isEqualTo("qlalfqjsgh");
        assertThat(httpRequest.getBodyParamValue("email")).isEqualTo("emailemailemail@gmail.com");
    }

}