package codesquad.webserver.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.parser.enums.HttpMethod;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HttpParserTest {

    @Test
    @DisplayName("정상적인 GET 요청을 파싱해야 한다")
    public void testParseValidGetRequest() throws Exception {
        // Given
        String request = "GET /index.html HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Connection: keep-alive\r\n\r\n";
        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(request.getBytes()));

        // When
        HttpRequest httpRequest = HttpParser.parse(in);

        // Then
        assertEquals(HttpMethod.GET, httpRequest.getRequestLine().getMethod());
        assertEquals("/index.html", httpRequest.getRequestLine().getPath());
        assertEquals("HTTP/1.1", httpRequest.getRequestLine().getHttpVersion());
        assertEquals(List.of("localhost"), httpRequest.getHeaders().get("Host"));
        assertEquals(List.of("keep-alive"), httpRequest.getHeaders().get("Connection"));
        assertEquals(Map.of(), httpRequest.getParams());
        assertEquals("", httpRequest.getBody());
    }

    @Test
    @DisplayName("정상적인 GET 요청을 파싱해야 한다 - Query String 포함")
    public void testParseValidGetRequestWithQueryString() throws Exception {
        // Given
        String request = "GET /search?query=java HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Connection: keep-alive\r\n\r\n";
        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(request.getBytes()));

        // When
        HttpRequest httpRequest = HttpParser.parse(in);

        // Then
        assertEquals(HttpMethod.GET, httpRequest.getRequestLine().getMethod());
        assertEquals("/search?query=java", httpRequest.getRequestLine().getFullPath());
        assertEquals("HTTP/1.1", httpRequest.getRequestLine().getHttpVersion());
        assertEquals(List.of("localhost"), httpRequest.getHeaders().get("Host"));
        assertEquals(List.of("keep-alive"), httpRequest.getHeaders().get("Connection"));
        assertEquals(Map.of("query", "java"), httpRequest.getParams());
        assertEquals("", httpRequest.getBody());
    }

    @Test
    @DisplayName("잘못된 요청 라인을 파싱할 때 IOException을 발생시켜야 한다")
    public void testParseInvalidRequestLine() {
        // Given
        String request = "INVALID_REQUEST_LINE\r\n" +
                "Host: localhost\r\n\r\n";
        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(request.getBytes()));

        // When & Then
        IOException exception = assertThrows(IOException.class, () -> {
            HttpParser.parse(in);
        });

        assertEquals("Invalid request line: INVALID_REQUEST_LINE", exception.getMessage());
    }
}
