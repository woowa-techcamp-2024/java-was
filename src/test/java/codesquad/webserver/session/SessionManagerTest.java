package codesquad.webserver.session;

import codesquad.application.domain.User;
import codesquad.application.session.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SessionManagerTest {
    private SessionManager sessionManager;
    private User testUser;

    @BeforeEach
    void setUp() {
        sessionManager = SessionManager.getInstance();
        testUser = new User("testUser", "password", "nickname");
    }

    @Test
    void testSingletonInstance() {
        SessionManager anotherSessionManager = SessionManager.getInstance();
        assertEquals(sessionManager, anotherSessionManager, "getInstance should always return the same instance");
    }

    @Test
    void testAddUser() {
        String key = sessionManager.addUser(testUser);
        assertNotNull(key, "Key should not be null");
        assertTrue(sessionManager.hasUser(key), "Session should contain the added user");
    }

    @Test
    void testGetUser() {
        String key = sessionManager.addUser(testUser);
        User retrievedUser = sessionManager.getUser(key);
        assertEquals(testUser, retrievedUser, "Retrieved user should be the same as added user");
    }

    @Test
    void testDeleteUser() {
        String key = sessionManager.addUser(testUser);
        sessionManager.deleteUser(key);
        assertFalse(sessionManager.hasUser(key), "User should be removed after deletion");
    }

    @Test
    void testHasUser() {
        String key = sessionManager.addUser(testUser);
        assertTrue(sessionManager.hasUser(key), "Session should have the user after adding");
        sessionManager.deleteUser(key);
        assertFalse(sessionManager.hasUser(key), "Session should not have the user after deletion");
    }

    @Test
    void testGetNonExistentUser() {
        User user = sessionManager.getUser("nonExistentKey");
        assertNull(user, "Getting a non-existent user should return null");
    }
}
