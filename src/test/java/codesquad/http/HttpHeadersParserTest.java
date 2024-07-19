package codesquad.http;

import codesquad.error.HttpRequestException;
import codesquad.http.parser.HttpHeadersParser;
import codesquad.http.parser.ParsersFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HttpHeadersParserTest {

    HttpHeadersParser headersParser;

    @BeforeEach
    void setUp() {
        headersParser = ParsersFactory.getHeadersParser();
    }

    @Nested
    class parse_메서드는 {

        @Nested
        class 비어있는_값이_들어오면 {

            String emptyHeaders = "";

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> headersParser.parse(emptyHeaders))
                        .isInstanceOf(HttpRequestException.class)
                        .hasMessageContaining("[ERROR] HTTP header의 내용이 없습니다.");
            }
        }

        @Nested
        class null이_들어오면 {

            String nullHeaders = null;

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> headersParser.parse(nullHeaders))
                        .isInstanceOf(HttpRequestException.class)
                        .hasMessageContaining("[ERROR] HTTP header의 내용이 없습니다.");
            }
        }

        @Nested
        class 정상적인_HTTP_header가_들어오면 {

            String headersText = "Host: localhost:8080\r\n" +
                    "Accept: text/html\r\n";

            @Test
            void 성공적으로_파싱해_HttpHeaders_인스턴스를_생성한다() {
                HttpHeaders httpHeaders = headersParser.parse(headersText);
                assertAll(
                        () -> assertThat(httpHeaders).isNotNull(),
                        () -> assertThat(httpHeaders.getValues(HttpHeaders.ACCEPT)).containsExactly("text/html")
                );
            }
        }
    }
}
