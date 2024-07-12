package codesquad.http.session;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

class SessionContextHolderTest {

    @AfterEach
    void tearDown() {
        SessionContextHolder.clear();
    }

    @Nested
    class setSessionId_메서드는 {

        @Test
        void 문자열_값을_저장한다() {
            String sessionId = "test";
            SessionContextHolder.setSessionId(sessionId);
            assertThat(SessionContextHolder.getSessionId()).isEqualTo(sessionId);
        }
    }

    @Nested
    class getSessionId_메서드는 {

        @Test
        void 저장된_값을_반환한다() {
            String sessionId = "test";
            SessionContextHolder.setSessionId(sessionId);
            assertThat(SessionContextHolder.getSessionId()).isEqualTo(sessionId);
        }
    }

    @Nested
    class clear_메서드는 {

        @Test
        void 저장된_값을_제거한다() {
            String sessionId = "test";
            SessionContextHolder.setSessionId(sessionId);
            SessionContextHolder.clear();
            assertThat(SessionContextHolder.getSessionId()).isNull();
        }
    }

    @Nested
    class 멀티_스레드_환경에서 {

        @Test
        void 각_스레드별로_값을_저장한다() throws ExecutionException, InterruptedException {
            String sessionId = "thread1";
            String anotherThreadSessionId = "thread2";
            SessionContextHolder.setSessionId(sessionId);

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<?> future = executorService.submit(() -> {
                SessionContextHolder.setSessionId(anotherThreadSessionId);
                assertThat(SessionContextHolder.getSessionId()).isEqualTo(anotherThreadSessionId);
                return SessionContextHolder.getSessionId();
            });
            assertThat(SessionContextHolder.getSessionId()).isEqualTo(sessionId);
            assertThat(SessionContextHolder.getSessionId()).isNotEqualTo(future.get());
            assertThat(future.get()).isEqualTo(anotherThreadSessionId);
        }
    }
}