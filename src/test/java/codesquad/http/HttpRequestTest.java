package codesquad.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.entry;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRequestTest {

    private static final String INPUT_STREAM_STR = """
        GET /index.html HTTP/1.1
        Host: localhost:8080
        Sec-Fetch-Site: none
        Connection: keep-alive
        Upgrade-Insecure-Requests: 1
        Sec-Fetch-Mode: navigate
        Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
        User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.5 Safari/605.1.15
        Accept-Language: ko-KR,ko;q=0.9
        Sec-Fetch-Dest: document
        Accept-Encoding: gzip, deflate
        """;

    private InputStream inputStream;

    @BeforeEach
    void init() {
        inputStream = new ByteArrayInputStream(INPUT_STREAM_STR.getBytes());
    }

    @Test
    @DisplayName("inputStream으로부터 HttpRequest 객체를 생성한다.")
    void create() {
        // Act
        var actualResult = HttpRequest.from(inputStream);

        // Assert
        assertAll(
            () -> assertThat(actualResult).isNotNull(),
            () -> assertThat(actualResult).isInstanceOf(HttpRequest.class),
            () -> assertThat(actualResult.getHttpMethod()).isEqualTo("GET"),
            () -> assertThat(actualResult.getRequestUri()).isEqualTo("/index.html"),
            () -> assertThat(actualResult.getHttpVersion()).isEqualTo("HTTP/1.1"),
            () -> assertThat(actualResult.getHeaders())
                .contains(
                    entry("Host", "localhost:8080"),
                    entry("Sec-Fetch-Site", "none"),
                    entry("Connection", "keep-alive"),
                    entry("Upgrade-Insecure-Requests", "1"),
                    entry("Sec-Fetch-Mode", "navigate"),
                    entry("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
                    entry("User-Agent",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.5 Safari/605.1.15"),
                    entry("Accept-Language", "ko-KR,ko;q=0.9"),
                    entry("Sec-Fetch-Dest", "document"),
                    entry("Accept-Encoding", "gzip, deflate"))
        );

    }

    @Test
    @DisplayName("요청 라인이 없는 경우 IllegalArgumentException을 던진다.")
    void createWhenRequestLineIsNull() {
        // Arrange
        var emptyInputStream = new ByteArrayInputStream("".getBytes());

        // Act & Assert
        assertThatThrownBy(() -> HttpRequest.from(emptyInputStream))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("요청 라인이 없습니다.");
    }

    @Test
    @DisplayName("요청 라인이 올바르지 않은 경우 IllegalArgumentException을 던진다.")
    void createWhenRequestLineIsInvalid() {
        // Arrange
        var invalidInputStream = new ByteArrayInputStream("GET /index.html".getBytes());

        // Act & Assert
        assertThatThrownBy(() -> HttpRequest.from(invalidInputStream))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("요청 라인이 올바르지 않습니다.");
    }
}