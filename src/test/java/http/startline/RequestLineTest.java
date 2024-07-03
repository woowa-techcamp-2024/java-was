package http.startline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import http.HttpMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestLineTest {

    @Test
    @DisplayName("RequestLine 객체 생성 테스트")
    public void testFromRequest_ValidStringLine() {
        String requestLine = "GET /index.html HTTP/1.1";
        RequestLine startLine = RequestLine.fromString(requestLine);

        Assertions.assertEquals(HttpMethod.GET, startLine.getMethod());
        assertEquals("/index.html", startLine.getPath().getPath());
        assertEquals("HTTP/1.1", startLine.getVersion());
    }

    @Test
    @DisplayName("RequestLine 객체 생성 테스트 - 잘못된 형식")
    public void testFromRequest_InvalidStringLine() {
        String requestLine = "GET /index.html";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RequestLine.fromString(requestLine);
        });

        assertEquals("Request Line 형식이 잘못되었습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("RequestLine 객체 생성 테스트 - HTTP 메서드")
    public void testGetMethod() {
        RequestLine requestLine = RequestLine.fromString("GET /index.html HTTP/1.1");
        assertEquals(HttpMethod.GET, requestLine.getMethod());
    }

    @Test
    @DisplayName("RequestLine 객체 생성 테스트 - 경로")
    public void testGetPath() {
        RequestLine requestLine = RequestLine.fromString("GET /index.html HTTP/1.1");
        assertEquals("/index.html", requestLine.getPath().getPath());
    }

    @Test
    @DisplayName("RequestLine 객체 생성 테스트 - 버전")
    public void testGetVersion() {
        RequestLine requestLine = RequestLine.fromString("GET /index.html HTTP/1.1");
        assertEquals("HTTP/1.1", requestLine.getVersion());
    }

}