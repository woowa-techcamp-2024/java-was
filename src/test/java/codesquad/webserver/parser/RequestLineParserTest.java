package codesquad.webserver.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.parser.enums.HttpMethod;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RequestLineParserTest {

    private HttpRequest request;

    @BeforeEach
    public void setUp() {
        request = new HttpRequest();
    }

    @Test
    @DisplayName("정상적인 요청 라인을 파싱해야 한다")
    public void testParseValidRequestLine() throws IOException {
        // Given
        String requestLine = "GET /index.html HTTP/1.1";
        BufferedReader in = new BufferedReader(new StringReader(requestLine));

        // When
        RequestLineParser.parse(in, request);
        RequestLine result = request.getRequestLine();

        // Then
        assertEquals(HttpMethod.GET, result.getMethod());
        assertEquals("/index.html", result.getPath());
        assertEquals("HTTP/1.1", result.getHttpVersion());
    }

    @Test
    @DisplayName("잘못된 요청 라인을 파싱할 때 IOException을 발생시켜야 한다")
    public void testParseInvalidRequestLine() {
        // Given
        String requestLine = "INVALID_REQUEST_LINE";
        BufferedReader in = new BufferedReader(new StringReader(requestLine));

        // When & Then
        IOException exception = assertThrows(IOException.class, () -> {
            RequestLineParser.parse(in, request);
        });

        assertEquals("Invalid request line: " + requestLine, exception.getMessage());
    }

    @Test
    @DisplayName("요청 라인이 null인 경우 IOException을 발생시켜야 한다")
    public void testParseNullRequestLine() {
        // Given
        BufferedReader in = new BufferedReader(new StringReader(""));

        // When & Then
        IOException exception = assertThrows(IOException.class, () -> {
            RequestLineParser.parse(in, request);
        });

        assertEquals("Invalid request line", exception.getMessage());
    }

    @Test
    @DisplayName("요청 라인이 세 부분으로 나뉘지 않을 경우 IOException을 발생시켜야 한다")
    public void testParseRequestLineWithInvalidParts() {
        // Given
        String requestLine = "GET /index.html";
        BufferedReader in = new BufferedReader(new StringReader(requestLine));

        // When & Then
        IOException exception = assertThrows(IOException.class, () -> {
            RequestLineParser.parse(in, request);
        });

        assertEquals("Invalid request line: " + requestLine, exception.getMessage());
    }
}
