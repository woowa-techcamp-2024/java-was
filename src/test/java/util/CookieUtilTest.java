package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class CookieUtilTest {

    @Test
    void testGetCookieValueExists() {
        String cookie = "sessionId=abc123; userId=xyz789";
        String key = "sessionId";
        String expectedValue = "abc123";

        String actualValue = CookieUtil.getCookieValue(cookie, key);

        assertEquals(expectedValue, actualValue, "The method should return the correct session ID value.");
    }

    @Test
    void testGetCookieValueDoesNotExist() {
        String cookie = "sessionId=abc123; userId=xyz789";
        String key = "nonExistingKey";

        String actualValue = CookieUtil.getCookieValue(cookie, key);

        assertNull(actualValue, "The method should return null for a non-existing key.");
    }
}