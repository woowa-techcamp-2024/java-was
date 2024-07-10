package codesquad.http.session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SessionManagerTest {

    SessionManager sessionManager;

    @BeforeEach
    void setUp() {
        sessionManager = SessionManager.getInstance();
    }

    @Nested
    class createSession_메서드는 {

        @Test
        void 유니크한_세션ID를_생성한다() {
            String sessionId1 = sessionManager.createSession("user1");
            String sessionId2 = sessionManager.createSession("user2");
            assertThat(sessionId1).isNotEqualTo(sessionId2);
        }

        @Test
        void 이미_세션이_존재하면_기존_세션ID를_반환한다() {
            String userId = "user1";
            String sessionId1 = sessionManager.createSession(userId);
            String sessionId2 = sessionManager.createSession(userId);
            assertThat(sessionId1).isEqualTo(sessionId2);
        }
    }
}