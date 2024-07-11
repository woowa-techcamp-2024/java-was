package codesquad.webserver.http;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HttpRequestBodyParseTest {
    @Test
    void testParseForm() {
        String queryString = "name=John%20Doe&age=30&city=New%20York";
        Map<String, Object> result = HttpRequestBodyParser.parseForm(queryString);

        assertEquals(3, result.size());
        assertEquals("John Doe", result.get("name"));
        assertEquals("30", result.get("age"));
        assertEquals("New York", result.get("city"));
    }

    @Test
    void testParseFormWithEmptyString() {
        Map<String, Object> result = HttpRequestBodyParser.parseForm("");
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseFormWithNullString() {
        Map<String, Object> result = HttpRequestBodyParser.parseForm(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseJson() {
        String jsonString = "{\"name\":\"John Doe\",\"age\":30,\"city\":\"New York\",\"isStudent\":false,\"grade\":null}";
        Map<String, Object> result = HttpRequestBodyParser.parseJson(jsonString);

        assertEquals(5, result.size());
        assertEquals("John Doe", result.get("name"));
        assertEquals(30L, result.get("age"));
        assertEquals("New York", result.get("city"));
        assertEquals(false, result.get("isStudent"));
        assertNull(result.get("grade"));
    }

    @Test
    void testParseJsonWithNestedObject() {
        String jsonString = "{\"name\":\"John Doe\",\"address\":{\"city\":\"New York\",\"zip\":\"10001\"}}";
        Map<String, Object> result = HttpRequestBodyParser.parseJson(jsonString);

        assertEquals(2, result.size());
        assertEquals("John Doe", result.get("name"));
        assertInstanceOf(Map.class, result.get("address"));

        @SuppressWarnings("unchecked")
        Map<String, Object> address = (Map<String, Object>) result.get("address");
        assertEquals("New York", address.get("city"));
        assertEquals("10001", address.get("zip"));
    }

    @Test
    void testParseJsonWithEmptyObject() {
        String jsonString = "{}";
        Map<String, Object> result = HttpRequestBodyParser.parseJson(jsonString);
        assertTrue(result.isEmpty());
    }
}
