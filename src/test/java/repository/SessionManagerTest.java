package repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import session.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import session.SessionManager;

class SessionManagerTest {

    private SessionManager sessionManager;

    @BeforeEach
    void setUp() {
        sessionManager = SessionManager.getInstance();
    }

    @Test
    void testCreateSession() {
        Session session = sessionManager.createSession();
        assertNotNull(session, "세션 생성 실패");
    }

    @Test
    void testGetSession() {
        Session newSession = sessionManager.createSession();
        Session fetchedSession = sessionManager.getSession(newSession.getSessionId());
        assertEquals(newSession, fetchedSession, "세션 조회 실패");
    }

    @Test
    void testInvalidateSession() {
        Session session = sessionManager.createSession();
        sessionManager.invalidateSession(session.getSessionId());
        assertNull(sessionManager.getSession(session.getSessionId()), "세션 무효화 실패");
    }

    @Test
    void testFindOrCreateEmptySession() {
        Session session = sessionManager.findOrCreateEmptySession();
        assertNotNull(session, "빈 세션 찾기 또는 생성 실패");
    }

    @Test
    void testGetSessionWithInvalidId() {
        assertNull(sessionManager.getSession(1234), "존재하지 않는 세션 ID로 조회했을 때 null이 반환되어야 합니다.");
    }
}
