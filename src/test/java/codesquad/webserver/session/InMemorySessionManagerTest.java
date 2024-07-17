package codesquad.webserver.session;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.webserver.db.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InMemorySessionManagerTest {

    private InMemorySessionManager sessionManager;
    private User user;

    @BeforeEach
    void setUp() {
        sessionManager = new InMemorySessionManager();
        user = new User("1", "password", "테스트 유저");
    }

    @Test
    @DisplayName("세션을 생성하고 조회한다")
    void testCreateAndRetrieveSession() {
        Session session = sessionManager.createSession(user);
        assertThat(session).isNotNull();
        assertThat(session.getUser()).isEqualTo(user);

        Session retrievedSession = sessionManager.getSession(session.getId());
        assertThat(retrievedSession).isNotNull();
        assertThat(retrievedSession.getId()).isEqualTo(session.getId());
        assertThat(retrievedSession.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("세션을 제거한다")
    void testRemoveSession() {
        Session session = sessionManager.createSession(user);
        assertThat(sessionManager.getSession(session.getId())).isNotNull();

        sessionManager.removeSession(session.getId());
        assertThat(sessionManager.getSession(session.getId())).isNull();
    }

    @Test
    @DisplayName("만료된 세션을 무효화한다")
    void testInvalidateExpiredSessions() throws InterruptedException {
        // 세션 타임아웃을 테스트하기 위해 짧은 시간으로 설정
        sessionManager = new InMemorySessionManager() {
            @Override
            public void invalidateExpiredSessions() {
                long now = System.currentTimeMillis();
                sessionManager.getSessions().entrySet().removeIf(entry ->
                        now - entry.getValue().getCreationTime() > 1000 // 1초
                );
            }
        };

        Session session = sessionManager.createSession(user);
        Thread.sleep(1500); // 1.5초 대기하여 세션이 만료되도록 함

        sessionManager.invalidateExpiredSessions();
        assertThat(sessionManager.getSession(session.getId())).isNull();
    }

    @Test
    @DisplayName("세션을 무효화한다")
    void testInvalidateSession() {
        Session session = sessionManager.createSession(user);
        assertThat(sessionManager.getSession(session.getId())).isNotNull();

        sessionManager.invalidateSession(session.getId());
        assertThat(sessionManager.getSession(session.getId())).isNull();
    }

    @Test
    @DisplayName("모든 세션을 제거한다")
    void testClearAllSessions() {
        Session session1 = sessionManager.createSession(user);
        User user2 = new User("2", "password2", "테스트 유저 2");
        Session session2 = sessionManager.createSession(user2);

        assertThat(sessionManager.getSession(session1.getId())).isNotNull();
        assertThat(sessionManager.getSession(session2.getId())).isNotNull();

        sessionManager.clearAllSessions();
        assertThat(sessionManager.getSession(session1.getId())).isNull();
        assertThat(sessionManager.getSession(session2.getId())).isNull();
    }

    @Test
    @DisplayName("세션 ID를 올바르게 생성한다")
    void testGenerateSessionId() {
        String sessionId1 = sessionManager.createSession(user).getId();
        String sessionId2 = sessionManager.createSession(user).getId();

        assertThat(sessionId1).isNotEqualTo(sessionId2);
        assertThat(sessionId1).hasSize(16);
        assertThat(sessionId2).hasSize(16);
    }
}
