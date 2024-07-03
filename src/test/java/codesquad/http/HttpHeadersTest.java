package codesquad.http;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HttpHeadersTest {

    HttpHeaders httpHeaders;

    @Nested
    class fromText_메서드는 {

        @Nested
        class 비어있는_값이_들어오면 {

            String emptyHeaders = "";

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> HttpHeaders.fromText(emptyHeaders))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("[ERROR] HTTP header의 내용이 없습니다.");
            }
        }

        @Nested
        class null이_들어오면 {

            String nullHeaders = null;

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> HttpHeaders.fromText(nullHeaders))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("[ERROR] HTTP header의 내용이 없습니다.");
            }
        }

        @Nested
        class 정상적인_HTTP_header가_들어오면 {

            String request = "Host: localhost:8080\n" +
                    "Accept: text/html\n" +
                    "\n";

            @Test
            void 성공적으로_파싱할_수_있다() {
                httpHeaders = HttpHeaders.fromText(request);
                assertAll(
                        () -> assertThat(httpHeaders).isNotNull(),
                        () -> assertThat(httpHeaders.getValues("Accept")).containsExactly("text/html")
                );
            }
        }
    }
}
