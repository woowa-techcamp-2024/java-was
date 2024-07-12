package codesquad.http.session;

import codesquad.http.HttpHeaders;
import codesquad.http.HttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SessionContextFactoryTest {

    SessionContextFactory sessionContextFactory;

    @BeforeEach
    void setUp() {
        sessionContextFactory = SessionContextFactory.getInstance();
    }

    @Nested
    class createSessionContext_메서드는 {

        @Test
        void HttpRequest의_sessionId를_SessionContextHolder에_저장한다() {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.addValue("Cookie", "sid=test");
            HttpRequest httpRequest = new HttpRequest(null, null, null, null, httpHeaders, null);

            sessionContextFactory.createSessionContext(httpRequest);

            assertThat(SessionContextHolder.getSessionId()).isEqualTo("test");
        }
    }
}