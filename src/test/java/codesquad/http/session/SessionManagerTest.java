package codesquad.http.session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

class SessionManagerTest {

    private final Logger log = LoggerFactory.getLogger(SessionManagerTest.class);

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

    @Nested
    class findUserId_메서드는 {

        @Test
        void sessionId에_해당하는_userId를_반환한다() {
            String sessionId = sessionManager.createSession("user1");
            assertThat(sessionManager.findUserId(sessionId)).contains("user1");
        }

        @Test
        void sessionId가_null이면_빈_Optional을_반환한다() {
            assertThat(sessionManager.findUserId(null)).isEmpty();
        }

        @Test
        void sessionId가_존재하지_않는_값이면_빈_Optional을_반환한다() {
            assertThat(sessionManager.findUserId("non-exist-session-id")).isEmpty();
        }
    }
}