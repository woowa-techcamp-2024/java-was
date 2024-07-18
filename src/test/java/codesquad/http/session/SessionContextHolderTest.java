package codesquad.http.session;

import codesquad.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

class SessionContextHolderTest {

    String sessionId;
    User user;

    @AfterEach
    void tearDown() {
        SessionContextHolder.clear();
        sessionId = "test";
        user = new User("userId", "nickname", "password");
    }

    @Nested
    class setSessionId_메서드는 {

        @Test
        void SessionContext_인스턴스를_저장한다() {
            SessionContextHolder.setContext(sessionId, user);
            SessionContext context = SessionContextHolder.getContext();
            assertThat(context.sessionId()).isEqualTo(sessionId);
            assertThat(context.user()).isEqualTo(user);
        }
    }

    @Nested
    class getSessionId_메서드는 {

        @Test
        void 저장된_인스턴스를_반환한다() {
            SessionContextHolder.setContext(sessionId, user);
            SessionContext context = SessionContextHolder.getContext();
            assertThat(context.sessionId()).isEqualTo(sessionId);
            assertThat(context.user()).isEqualTo(user);
        }
    }

    @Nested
    class clear_메서드는 {

        @Test
        void 저장된_인스턴스를_제거한다() {
            SessionContextHolder.setContext(sessionId, user);
            SessionContextHolder.clear();
            assertThat(SessionContextHolder.getContext()).isNull();
        }
    }

    @Nested
    class 멀티_스레드_환경에서 {

        @Test
        void 각_스레드별로_값을_저장한다() throws ExecutionException, InterruptedException {
            String sessionId1 = "thread1";
            String sessionId2 = "thread2";
            User user1 = new User("userId1", "nickname1", "password1");
            User user2 = new User("userId2", "nickname2", "password2");

            SessionContextHolder.setContext(sessionId1, user1);

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                SessionContextHolder.setContext(sessionId2, user2);
                SessionContext context = SessionContextHolder.getContext();
                assertThat(context.sessionId()).isEqualTo(sessionId2);
                assertThat(context.user()).isEqualTo(user2);
            });

            SessionContext context = SessionContextHolder.getContext();
            assertThat(context.sessionId()).isEqualTo(sessionId1);
            assertThat(context.user()).isEqualTo(user1);
        }
    }
}