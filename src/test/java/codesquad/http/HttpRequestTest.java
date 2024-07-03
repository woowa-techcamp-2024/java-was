package codesquad.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HttpRequestTest {

    HttpRequest httpRequest;

    @Nested
    class fromText_메서드는 {

        @Nested
        class 비어있는_값이_들어오면 {

            String emptyRequest = "";

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> HttpRequest.fromText(emptyRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] HTTP request의 내용이 없습니다.");
            }
        }

        @Nested
        class null이_들어오면 {

            String nullRequest = null;

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> HttpRequest.fromText(nullRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] HTTP request의 내용이 없습니다.");
            }
        }

        @Nested
        class 정상적인_HTTP_request가_들어오면 {

            String request = "GET /index.html HTTP/1.1\n" +
                    "Host: localhost:8080\n" +
                    "Accept: text/html\n" +
                    "\n";

            @Test
            void 성공적으로_파싱할_수_있다() {
                httpRequest = HttpRequest.fromText(request);
                assertAll(
                        () -> assertThat(httpRequest).isNotNull(),
                        () -> assertThat(httpRequest.uri()).isEqualTo("/index.html"),
                        () -> assertThat(httpRequest.method()).isEqualTo("GET"),
                        () -> assertThat(httpRequest.headers()).isNotNull()
                );
            }
        }
    }
}
