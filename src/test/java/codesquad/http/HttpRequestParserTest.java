package codesquad.http;

import codesquad.http.parser.HttpRequestParser;
import codesquad.http.parser.ParsersFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HttpRequestParserTest {

    HttpRequestParser requestParser;

    @BeforeEach
    void setUp() {
        requestParser = ParsersFactory.getHttpRequestParser();
    }

    @Nested
    class parse_메서드는 {

        @Nested
        class 비어있는_값이_들어오는_경우 {

            String emptyRequest = "";

            @Test
            void null을_반환한다() {
                HttpRequest httpRequest = requestParser.parse(emptyRequest);
                assertThat(httpRequest).isNull();
            }
        }

        @Nested
        class null이_들어오는_경우 {

            String nullRequest = null;

            @Test
            void null을_반환한다() {
                HttpRequest httpRequest = requestParser.parse(nullRequest);
                assertThat(httpRequest).isNull();
            }
        }

        @Nested
        class 정상적인_HTTP_request가_들어오는_경우 {

            String request = "GET /index.html HTTP/1.1\r\n" +
                    "Host: localhost:8080\r\n" +
                    "Accept: text/html\r\n" +
                    "\r\n";

            @Test
            void 성공적으로_파싱해_HttpRequest_인스턴스를_생성한다() {
                HttpRequest httpRequest = requestParser.parse(request);
                assertAll(
                        () -> assertThat(httpRequest).isNotNull(),
                        () -> assertThat(httpRequest.uri()).isEqualTo("/index.html"),
                        () -> assertThat(httpRequest.method()).isEqualTo("GET"),
                        () -> assertThat(httpRequest.headerValues(HttpHeaders.ACCEPT)).containsExactly("text/html"),
                        () -> assertThat(httpRequest.headers()).isNotNull()
                );
            }
        }
    }
}
