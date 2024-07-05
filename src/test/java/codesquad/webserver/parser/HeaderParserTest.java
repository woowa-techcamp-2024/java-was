package codesquad.webserver.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HeaderParserTest {


    @Test
    @DisplayName("헤더를 정상적으로 파싱해야 한다")
    public void testParseHeaders() throws IOException {
        // Given
        String headersString = "Content-Type: application/json\r\n" +
                "Content-Length: 123\r\n" +
                "Host: localhost\r\n" +
                "\r\n";
        BufferedReader in = new BufferedReader(new StringReader(headersString));

        // When
        Map<String, String> headers = HeaderParser.parse(in);

        // Then
        assertEquals(3, headers.size(), "헤더의 개수가 3개여야 합니다.");
        assertEquals("application/json", headers.get("Content-Type"));
        assertEquals("123", headers.get("Content-Length"));
        assertEquals("localhost", headers.get("Host"));
    }

    @Test
    @DisplayName("빈 헤더는 빈 맵을 반환해야 한다")
    public void testParseEmptyHeaders() throws IOException {
        // Given
        String headersString = "\r\n";
        BufferedReader in = new BufferedReader(new StringReader(headersString));

        // When
        Map<String, String> headers = HeaderParser.parse(in);

        // Then
        assertTrue(headers.isEmpty(), "빈 헤더 문자열은 빈 맵을 반환해야 합니다.");
    }

    @Test
    @DisplayName("잘못된 형식의 헤더는 무시해야 한다")
    public void testParseInvalidHeaders() throws IOException {
        // Given
        String headersString = "Content-Type: application/json\r\n" +
                "Invalid-Header-Without-Colon\r\n" +
                "Host: localhost\r\n" +
                "\r\n";
        BufferedReader in = new BufferedReader(new StringReader(headersString));

        // When
        Map<String, String> headers = HeaderParser.parse(in);

        // Then
        assertEquals(2, headers.size(), "유효한 헤더의 개수가 2개여야 합니다.");
        assertEquals("application/json", headers.get("Content-Type"));
        assertEquals("localhost", headers.get("Host"));
    }
}
