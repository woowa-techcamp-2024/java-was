package codesquad.webserver.http;

import static codesquad.utils.string.StringUtils.CRLF;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class HttpResponseTest {

    @Nested
    class Describe_SetContentType {

        @ParameterizedTest
        @EnumSource(HttpMediaType.class)
        @DisplayName("[Success] 응답 헤더가 Content-Type: value 로 설정된다")
        void contentType(HttpMediaType mediaType) {
            // given
            HttpResponse httpResponse = HttpResponse.ok();
            String expectedHeader = "Content-Type: " + mediaType.getName() + CRLF;
            // when
            httpResponse.setContentType(mediaType);
            // then
            String header = httpResponse.getHeaders();
            assertThat(header).isEqualTo(expectedHeader);
        }

    }

    @Nested
    class Describe_SetBody {

        @Test
        @DisplayName("[Success] 응답 헤더가 Content-Length: value 로 설정된다")
        void setBodyThenContentLength() {
            // given
            HttpResponse httpResponse = HttpResponse.ok();
            byte[] body = "Hello, World!".getBytes();
            int expectedLength = body.length;
            String expectedHeader = "Content-Length: " + expectedLength + CRLF;

            // when
            httpResponse.setBody(body);

            // then
            String header = httpResponse.getHeaders();
            assertThat(header).isEqualTo(expectedHeader);
        }
    }

    @Nested
    class Describe_SetDefaultHeaders {

        @Test
        @DisplayName("[Success] 기본 헤더들이 설정된다")
        void defaultHeaders() {
            // given
            HttpResponse httpResponse = HttpResponse.ok();
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            String expectedDateHeader = "Date: " + zonedDateTime.toString() + CRLF;
            String expectedServerHeader = "Server: Woowah WAS Server/1.0" + CRLF;
            String expectedConnectionHeader = "Connection: close" + CRLF;

            // when
            httpResponse.setDefaultHeaders(zonedDateTime);

            // then
            String headers = httpResponse.getHeaders();
            assertThat(headers).contains(expectedDateHeader);
            assertThat(headers).contains(expectedServerHeader);
            assertThat(headers).contains(expectedConnectionHeader);
        }
    }


    @Nested
    class Describe_AddCookie {

        @Test
        @DisplayName("[Success] 쿠키가 추가되고 Set-Cookie 헤더가 설정된다")
        void addCookie() {
            // given
            HttpResponse httpResponse = HttpResponse.ok();
            Cookie cookie = new Cookie("sessionId", "abc123");
            cookie.setPath("/");
            cookie.setDomain("woowahcamp.com");
            String expectedHeader = "Set-Cookie: sessionId=abc123; Path=/; Domain=woowahcamp.com" + CRLF;

            // when
            httpResponse.addCookie(cookie);

            // then
            List<Cookie> cookies = httpResponse.getCookies();
            assertThat(cookies).contains(cookie);

            String headers = httpResponse.getHeaders();
            assertThat(headers).isEqualTo(expectedHeader);
        }
    }

}