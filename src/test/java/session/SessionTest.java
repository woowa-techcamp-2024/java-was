package session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import model.User;
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
    void testSetAndGetUser() {
        User user = new User("user", "password", "name", "email");
        session.setUser(user);
        assertEquals(user, session.getUser(), "User ID should match the set value");
    }
}