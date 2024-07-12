package session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SessionTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session = new Session(1);
    }

    @Test
    void testSessionCreation() {
        assertNotNull(session, "Session should not be null");
        assertEquals(1, session.getSessionId(), "Session ID should be 1");
    }

    @Test
    void testSessionAttributesManagement() {
        session.setAttribute("key", "value");
        assertEquals("value", session.getAttribute("key"), "Attribute value should be 'value'");
        session.removeAttribute("key");
        assertNull(session.getAttribute("key"), "Attribute should be removed");
    }

    @Test
    void testSetAndGetUserId() {
        String userId = "user123";
        session.setUserId(userId);
        assertEquals(userId, session.getUserId(), "User ID should match the set value");
    }

    @Test
    void testSetUserIdWithInvalidValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            session.setUserId("");
        });
        String expectedMessage = "Invalid userId: userId must be non-null, non-empty and current userId must be null.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "Exception message should contain the expected text");
    }
}