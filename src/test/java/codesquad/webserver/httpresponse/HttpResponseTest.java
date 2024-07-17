package codesquad.webserver.httpresponse;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.webserver.session.cookie.HttpCookie;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpResponseTest {

    private HttpResponse response;

    @BeforeEach
    void setUp() {
        response = new HttpResponse();
    }

    @Test
    @DisplayName("HTTP 응답의 기본 상태 코드는 200이고 상태 메시지는 'OK'이다")
    void testDefaultStatusCodeAndMessage() {
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getStatusMessage()).isEqualTo("OK");
    }

    @Test
    @DisplayName("상태 코드를 설정할 수 있다")
    void testSetStatusCode() {
        response.setStatusCode(404);
        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("상태 메시지를 설정할 수 있다")
    void testSetStatusMessage() {
        response.setStatusMessage("Not Found");
        assertThat(response.getStatusMessage()).isEqualTo("Not Found");
    }

    @Test
    @DisplayName("헤더를 추가하고 가져올 수 있다")
    void testAddAndGetHeader() {
        response.addHeader("Content-Type", "text/html");
        assertThat(response.getHeader("Content-Type")).containsExactly("text/html");
    }

    @Test
    @DisplayName("여러 헤더를 추가하고 가져올 수 있다")
    void testAddAndGetMultipleHeaders() {
        response.addHeader("Set-Cookie", "cookie1=value1");
        response.addHeader("Set-Cookie", "cookie2=value2");
        assertThat(response.getHeader("Set-Cookie")).containsExactly("cookie1=value1", "cookie2=value2");
    }

    @Test
    @DisplayName("쿠키를 추가하고 가져올 수 있다")
    void testAddAndGetCookies() {
        HttpCookie cookie = new HttpCookie("session", "1234");
        response.addCookie(cookie);
        List<HttpCookie> cookies = response.getCookies();
        assertThat(cookies).hasSize(1);
        assertThat(cookies.get(0).getName()).isEqualTo("session");
        assertThat(cookies.get(0).getValue()).isEqualTo("1234");
    }

    @Test
    @DisplayName("응답 본문을 설정하고 가져올 수 있다")
    void testSetAndGetBody() {
        byte[] body = "Hello, World!".getBytes();
        response.setBody(body);
        assertThat(response.getBody()).isEqualTo(body);
    }

    @Test
    @DisplayName("HTTP 응답을 올바르게 생성한다")
    void testGenerateHttpResponse() {
        response.setStatusCode(200);
        response.setStatusMessage("OK");
        response.addHeader("Content-Type", "text/html");
        response.setBody("Hello, World!".getBytes());

        byte[] responseBytes = response.generateHttpResponse();
        String responseString = new String(responseBytes);

        assertThat(responseString).contains("HTTP/1.1 200 OK");
        assertThat(responseString).contains("Content-Type: text/html");
        assertThat(responseString).contains("Hello, World!");
    }

    @Test
    @DisplayName("빌더 패턴을 사용하여 HttpResponse 객체를 생성할 수 있다")
    void testBuilder() {
        HttpCookie cookie = new HttpCookie("session", "1234");

        HttpResponse response = HttpResponse.builder()
                .statusCode(200)
                .statusMessage("OK")
                .header("Content-Type", "text/html")
                .cookie(cookie)
                .body("Hello, World!".getBytes())
                .build();

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getStatusMessage()).isEqualTo("OK");
        assertThat(response.getHeader("Content-Type")).containsExactly("text/html");
        assertThat(response.getCookies()).hasSize(1);
        assertThat(response.getCookies().get(0).getName()).isEqualTo("session");
        assertThat(response.getCookies().get(0).getValue()).isEqualTo("1234");
        assertThat(response.getBody()).isEqualTo("Hello, World!".getBytes());
    }
}
