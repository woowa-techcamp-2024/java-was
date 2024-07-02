package codesquad.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import codesquad.http.enums.StatusCode;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpResponseTest {

    @Test
    @DisplayName("HttpResponse 객체를 생성한다.")
    void create() {
        // Arrange
        var expectedHttpVersion = "HTTP/1.1";
        var expectedStatusCode = StatusCode.OK;
        var expectedHeaders = Map.of("Content-Type", "text/html");
        var expectedRequestUri = "/index.html";
        // Act
        var actualResult = HttpResponse.of(expectedHttpVersion, expectedStatusCode, expectedHeaders,
            expectedRequestUri);
        // Assert
        assertAll(
            () -> assertNotNull(actualResult),
            () -> assertThat(actualResult.generateResponse())
                .isNotEmpty()
        );

    }
}