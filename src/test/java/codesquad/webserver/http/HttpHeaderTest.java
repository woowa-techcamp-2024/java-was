package codesquad.webserver.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

class HttpHeaderTest {

    private HttpHeader httpHeader;

    @BeforeEach
    void setUp() {
        httpHeader = new HttpHeader();
    }

    @Test
    void testAddAndGetValue() {
        httpHeader.add("Content-Type", "application/json");
        assertEquals("application/json", httpHeader.getValue("Content-Type"));
    }

    @Test
    void testGetKeys() {
        httpHeader.add("Content-Type", "application/json");
        httpHeader.add("Accept", "text/html");

        Set<String> keys = httpHeader.getKeys();
        assertTrue(keys.contains("Content-Type"));
        assertTrue(keys.contains("Accept"));
        assertEquals(2, keys.size());
    }

    @Test
    void testGetValueNonExistentKey() {
        assertNull(httpHeader.getValue("Non-Existent-Key"));
    }

    @Test
    void testToString() {
        httpHeader.add("Content-Type", "application/json");
        httpHeader.add("Accept", "text/html");

        String result = httpHeader.toString();
        assertTrue(result.contains("Content-Type=application/json"));
        assertTrue(result.contains("Accept=text/html"));
    }
}