package codesquad.http;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HttpCookiesTest {

    @Nested
    class addCookie_메서드는 {

        @Test
        void 쿠키를_추가한다() {
            HttpCookies cookies = new HttpCookies();
            List<HttpCookie> cookieList = cookies.getCookies();

            cookies.addCookie("name", "value");
            cookies.addCookie("Path", "/");

            assertThat(cookieList).hasSize(2);
            assertThat(cookieList.get(0)).isEqualTo(new HttpCookie("name", "value"));
        }
    }

    @Nested
    class toCookiesString_메서드는 {

        @Test
        void 쿠키_리스트를_RFC_6265스펙에_명시된_형태의_문자열로_변환한다() {
            HttpCookies cookies = new HttpCookies();
            cookies.addCookie("name", "value");
            cookies.addCookie("Path", "/");

            assertThat(cookies.toCookiesString()).isEqualTo("name=value; Path=/");
        }
    }
}