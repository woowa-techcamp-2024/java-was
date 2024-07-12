package codesquad.http;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HttpCookieTest {

    @Nested
    class toCookieString_메서드는 {

        @Test
        void 쿠키_이름과_값을_RFC_6265스펙에_명시된_형태의_문자열로_변환한다() {
            HttpCookie cookie = new HttpCookie("name", "value");
            assertThat(cookie.toCookieString()).isEqualTo("name=value");
        }
    }
}