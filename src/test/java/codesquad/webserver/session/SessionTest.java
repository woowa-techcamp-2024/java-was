package codesquad.webserver.session;

import codesquad.application.domain.User;
import codesquad.application.session.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {
    private Session session;
    private User testUser;

    @BeforeEach
    void setUp() {
        session = Session.getInstance();
        testUser = new User("testUser", "password", "nickname");
    }

    @Test
    void testSingletonInstance() {
        Session anotherSession = Session.getInstance();
        assertEquals(session, anotherSession, "getInstance should always return the same instance");
    }

    @Test
    void testAddUser() {
        String key = session.addUser(testUser);
        assertNotNull(key, "Key should not be null");
        assertTrue(session.hasUser(key), "Session should contain the added user");
    }

    @Test
    void testGetUser() {
        String key = session.addUser(testUser);
        User retrievedUser = session.getUser(key);
        assertEquals(testUser, retrievedUser, "Retrieved user should be the same as added user");
    }

    @Test
    void testDeleteUser() {
        String key = session.addUser(testUser);
        session.deleteUser(key);
        assertFalse(session.hasUser(key), "User should be removed after deletion");
    }

    @Test
    void testHasUser() {
        String key = session.addUser(testUser);
        assertTrue(session.hasUser(key), "Session should have the user after adding");
        session.deleteUser(key);
        assertFalse(session.hasUser(key), "Session should not have the user after deletion");
    }

    @Test
    void testGetNonExistentUser() {
        User user = session.getUser("nonExistentKey");
        assertNull(user, "Getting a non-existent user should return null");
    }
}
