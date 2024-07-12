package session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SessionManagerTest {

    private SessionManager sessionManager;

    @BeforeEach
    void setUp() {
        sessionManager = SessionManager.getInstance();
    }

    @Test
    void testSingletonInstance() {
        SessionManager anotherInstance = SessionManager.getInstance();
        assertSame(sessionManager, anotherInstance, "SessionManager should return the same instance");
    }

    @Test
    void testCreateSession() {
        Session session = sessionManager.createSession();
        assertNotNull(session, "Session should not be null");
        assertNotNull(sessionManager.getSession(session.getSessionId()),
            "Session should be retrievable");
    }

    @Test
    void testGetSession() {
        Session session = sessionManager.createSession();
        Session retrievedSession = sessionManager.getSession(session.getSessionId());
        assertEquals(session, retrievedSession, "Retrieved session should match the created session");
    }

    @Test
    void testInvalidateSession() {
        Session session = sessionManager.createSession();
        sessionManager.invalidateSession(session.getSessionId());
        assertNull(sessionManager.getSession(session.getSessionId()), "Session should be invalidated and not retrievable");
    }

    @Test
    void testFindOrCreateEmptySession() {
        // Assuming there's no empty session initially
        Session session = sessionManager.findOrCreateEmptySession();
        assertNotNull(session, "Should find or create a new session if no empty session exists");
    }
}