package codesquad.webserver.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.parser.enums.HttpMethod;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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
        String requestLine = "GET /index.html HTTP/1.1\r\n";
        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(requestLine.getBytes()));

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
        String requestLine = "INVALID_REQUEST_LINE\r\n";
        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(requestLine.getBytes()));

        // When & Then
        IOException exception = assertThrows(IOException.class, () -> {
            RequestLineParser.parse(in, request);
        });

        assertEquals("Invalid request line: INVALID_REQUEST_LINE", exception.getMessage());
    }

    @Test
    @DisplayName("요청 라인이 빈 문자열인 경우 IOException을 발생시켜야 한다")
    public void testParseEmptyRequestLine() {
        // Given
        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(new byte[0]));

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
        String requestLine = "GET /index.html\r\n";
        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(requestLine.getBytes()));

        // When & Then
        IOException exception = assertThrows(IOException.class, () -> {
            RequestLineParser.parse(in, request);
        });

        assertEquals("Invalid request line: GET /index.html", exception.getMessage());
    }
}
