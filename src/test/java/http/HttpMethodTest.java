package http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HttpMethodTest {

    @Test
    @DisplayName("유효한 HTTP 메서드 테스트")
    public void testValidHttpMethods() {
        assertEquals(HttpMethod.GET, HttpMethod.of("GET"));
        assertEquals(HttpMethod.POST, HttpMethod.of("POST"));
        assertEquals(HttpMethod.PUT, HttpMethod.of("PUT"));
        assertEquals(HttpMethod.DELETE, HttpMethod.of("DELETE"));
    }

    @Test
    @DisplayName("대소문자 구분 없이 유효한 HTTP 메서드 테스트")
    public void testValidHttpMethodsIgnoreCase() {
        assertEquals(HttpMethod.GET, HttpMethod.of("get"));
        assertEquals(HttpMethod.POST, HttpMethod.of("post"));
        assertEquals(HttpMethod.PUT, HttpMethod.of("put"));
        assertEquals(HttpMethod.DELETE, HttpMethod.of("delete"));
    }

    @Test
    @DisplayName("유효하지 않은 HTTP 메서드 테스트")
    public void testInvalidHttpMethod() {
        assertThrows(IllegalArgumentException.class, () -> HttpMethod.of("PATCH"));
        assertThrows(IllegalArgumentException.class, () -> HttpMethod.of("HEAD"));
        assertThrows(IllegalArgumentException.class, () -> HttpMethod.of("OPTIONS"));
    }

    @Test
    @DisplayName("null HTTP 메서드 테스트")
    public void testNullHttpMethod() {
        assertThrows(IllegalArgumentException.class, () -> HttpMethod.of(null));
    }

    @Test
    public void testEmptyHttpMethod() {
        assertThrows(IllegalArgumentException.class, () -> HttpMethod.of(""));
    }
}