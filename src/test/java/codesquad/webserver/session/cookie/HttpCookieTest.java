package codesquad.webserver.session.cookie;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpCookieTest {

    private HttpCookie httpCookie;

    @BeforeEach
    void setUp() {
        httpCookie = new HttpCookie("testCookie", "testValue");
    }

    @Test
    @DisplayName("쿠키 이름과 값을 올바르게 설정하고 조회한다")
    void testCookieAttributes() {
        assertThat(httpCookie.getName()).isEqualTo("testCookie");
        assertThat(httpCookie.getValue()).isEqualTo("testValue");
        assertThat(httpCookie.getPath()).isEqualTo("/");
        assertThat(httpCookie.getMaxAge()).isEqualTo(30 * 60 * 1000);
    }

    @Test
    @DisplayName("쿠키의 속성을 설정하고 올바르게 조회한다")
    void testSetAttributes() {
        ZonedDateTime expires = ZonedDateTime.now().plusDays(1);
        httpCookie.setDomain("example.com")
                .setPath("/test")
                .setExpires(expires)
                .setMaxAge(3600)
                .setSecure(true)
                .setHttpOnly(true)
                .setSameSite(HttpCookie.SameSite.STRICT);

        assertThat(httpCookie.getDomain()).isEqualTo("example.com");
        assertThat(httpCookie.getPath()).isEqualTo("/test");
        assertThat(httpCookie.getExpires()).isEqualTo(expires);
        assertThat(httpCookie.getMaxAge()).isEqualTo(3600);
        assertThat(httpCookie.isSecure()).isTrue();
        assertThat(httpCookie.isHttpOnly()).isTrue();
        assertThat(httpCookie.getSameSite()).isEqualTo(HttpCookie.SameSite.STRICT);
    }

    @Test
    @DisplayName("Set-Cookie 헤더를 올바르게 생성한다")
    void testToSetCookieHeader() {
        ZonedDateTime expires = ZonedDateTime.now().plusDays(1);
        httpCookie.setDomain("example.com")
                .setPath("/test")
                .setExpires(expires)
                .setMaxAge(3600)
                .setSecure(true)
                .setHttpOnly(true)
                .setSameSite(HttpCookie.SameSite.STRICT);

        String setCookieHeader = httpCookie.toSetCookieHeader();
        assertThat(setCookieHeader).contains("testCookie=testValue");
        assertThat(setCookieHeader).contains("Domain=example.com");
        assertThat(setCookieHeader).contains("Path=/test");
        assertThat(setCookieHeader).contains("Expires=");
        assertThat(setCookieHeader).contains("Max-Age=3600");
        assertThat(setCookieHeader).contains("Secure");
        assertThat(setCookieHeader).contains("HttpOnly");
        assertThat(setCookieHeader).contains("SameSite=STRICT");
    }

    @Test
    @DisplayName("추가 속성을 설정하고 조회한다")
    void testAdditionalAttributes() {
        httpCookie.setAttribute("CustomAttr", "CustomValue");
        assertThat(httpCookie.getAdditionalAttributes().get("CustomAttr")).isEqualTo("CustomValue");

        String setCookieHeader = httpCookie.toSetCookieHeader();
        assertThat(setCookieHeader).contains("CustomAttr=CustomValue");
    }
}
