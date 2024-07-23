package codesquad.was.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HttpRequestTest {

    @Nested
    class HttpRequest가_들어오면 {

        @Test
        void httpRequest_객체를_생성한다() throws IOException {
            // given
            String httpRequestValue =
                    "POST /submit-form HTTP/1.1\r\n"
                            + "Host: localhost\r\n"
                            + "Connection: keep-alive\r\n"
                            + "Content-Type: application/x-www-form-urlencoded\r\n"
                            + "Content-Length: " + "body=data".getBytes().length + "\r\n"
                            + "\r\n"
                            + "name=JohnDoe";
            InputStream clientInput = new ByteArrayInputStream(httpRequestValue.getBytes("UTF-8"));

            // when
            HttpRequest httpRequest = new HttpRequest(clientInput);

            // then
            assertAll(
                    () -> assertThat(httpRequest.getRequestPath()).isEqualTo("/submit-form"),
                    () -> assertThat(httpRequest.getHttpVersion()).isEqualTo("HTTP/1.1")
            );
        }

        @Test
        void URI에서_쿼리파라미터를_디코딩하고_파싱할_수_있다() throws IOException {
            // given
            String httpRequestValue =
                    "GET /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\r\n"
                            + "Host: localhost\r\n"
                            + "Connection: keep-alive\r\n"
                            + "Content-Type: application/x-www-form-urlencoded\r\n"
                            + "Content-Length: " + "body=data".getBytes().length + "\r\n"
                            + "\r\n"
                            + "body=data";
            InputStream clientInput = new ByteArrayInputStream(httpRequestValue.getBytes("UTF-8"));

            // when
            HttpRequest httpRequest = new HttpRequest(clientInput);

            // then
            assertAll(
                    () -> assertThat(httpRequest.getParameter("userId")).isEqualTo("javajigi"),
                    () -> assertThat(httpRequest.getParameter("password")).isEqualTo("password"),
                    () -> assertThat(httpRequest.getParameter("name")).isEqualTo("박재성"),
                    () -> assertThat(httpRequest.getParameter("email")).isEqualTo("javajigi@slipp.net")
            );
        }

        @Test
        void URI에서_쿼리파라미터의_value가_없으면_제외된다() throws IOException {
            // given
            String httpRequestValue =
                    "GET /create?userId=&password=&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\r\n"
                            + "Host: localhost\r\n"
                            + "Connection: keep-alive\r\n"
                            + "Content-Type: application/x-www-form-urlencoded\r\n"
                            + "Content-Length: " + "body=data".getBytes().length + "\r\n"
                            + "\r\n"
                            + "body=data";
            InputStream clientInput = new ByteArrayInputStream(httpRequestValue.getBytes("UTF-8"));

            // when
            HttpRequest httpRequest = new HttpRequest(clientInput);

            // then
            assertAll(
                    () -> assertThat(httpRequest.getParameter("userId")).isNull(),
                    () -> assertThat(httpRequest.getParameter("password")).isNull(),
                    () -> assertThat(httpRequest.getParameter("name")).isEqualTo("박재성"),
                    () -> assertThat(httpRequest.getParameter("email")).isEqualTo("javajigi@slipp.net")
            );
        }

        @Test
        void Request_Message_Body가_form_data_라면_파싱하여_저장한다() throws IOException {
            // given
            String httpRequestValue =
                    "GET /create HTTP/1.1\r\n"
                            + "Host: localhost\r\n"
                            + "Connection: keep-alive\r\n"
                            + "Content-Type: application/x-www-form-urlencoded\r\n"
                            + "Content-Length: "
                            + "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net".length()
                            + "\r\n"
                            + "\r\n"
                            + "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";
            InputStream clientInput = new ByteArrayInputStream(httpRequestValue.getBytes("UTF-8"));

            // when
            HttpRequest httpRequest = new HttpRequest(clientInput);

            // then
            assertAll(
                    () -> assertThat(httpRequest.getParameter("userId")).isEqualTo("javajigi"),
                    () -> assertThat(httpRequest.getParameter("password")).isEqualTo("password"),
                    () -> assertThat(httpRequest.getParameter("name")).isEqualTo("박재성"),
                    () -> assertThat(httpRequest.getParameter("email")).isEqualTo("javajigi@slipp.net")
            );
        }
    }

    @Nested
    class getCookies_메소드는 {

        @Test
        void 요청으로_들어온_쿠키목록을_반환한다() throws IOException {
            // given
            String httpRequestValue =
                    "GET /create HTTP/1.1\r\n"
                            + "Host: localhost\r\n"
                            + "Connection: keep-alive\r\n"
                            + "Cookie: key1=value1; key2=value2\r\n"
                            + "\r\n";
            InputStream clientInput = new ByteArrayInputStream(httpRequestValue.getBytes("UTF-8"));

            // when
            HttpRequest httpRequest = new HttpRequest(clientInput);
            List<Cookie> cookies = httpRequest.getCookies();

            // then
            assertThat(cookies).containsExactlyElementsOf(
                    List.of(new Cookie("key1", "value1"), new Cookie("key2", "value2")));
        }
    }
}
