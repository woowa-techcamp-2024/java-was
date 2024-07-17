package codesquad.webserver.filter;

import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.httpresponse.HttpResponseBuilder;
import codesquad.webserver.db.user.User;
import codesquad.webserver.parser.RequestLine;
import codesquad.webserver.parser.enums.HttpMethod;
import codesquad.webserver.session.Session;
import codesquad.webserver.session.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SessionCheckFilterTest {

    private SessionManager sessionManager;
    private SessionCheckFilter filter;
    private HttpRequest request;
    private HttpResponse response;
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        sessionManager = new TestSessionManager();
        filter = new SessionCheckFilter(sessionManager);

        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Cookie", List.of("SID=12345"));
        request = new HttpRequest(new RequestLine(HttpMethod.GET, "/user/list", "/user/list","HTTP/1.1"), headers, null, null);
        response = HttpResponseBuilder.ok().build();
        chain = new FilterChain();
    }

    @Test
    @DisplayName("유효한 세션이 없는 경우 로그인 페이지로 리디렉션한다")
    void testRedirectToLoginIfNoValidSession() {
        ((TestSessionManager) sessionManager).setValidSession(false);

        filter.doFilter(request, response, chain);

        assertThat(chain.getResponse().getStatusCode()).isEqualTo(302);
        assertThat(chain.getResponse().getHeader("Location")).contains("/login");
    }

    @Test
    @DisplayName("유효한 세션이 있는 경우 요청을 계속 처리한다")
    void testContinueWithValidSession() {
        ((TestSessionManager) sessionManager).setValidSession(true);

        filter.doFilter(request, response, chain);

        assertThat(chain.getResponse()).isNull();
    }

    // TestSessionManager는 테스트용 SessionManager 구현체입니다.
    private static class TestSessionManager implements SessionManager {
        private boolean validSession;


        @Override
        public Session createSession(User user) {
            return null;
        }

        @Override
        public Session getSession(String sessionId) {
            if (validSession) {
                return new Session() {
                    @Override
                    public String getId() {
                        return sessionId;
                    }

                    @Override
                    public User getUser() {
                        return null;
                    }

                    @Override
                    public void setAttribute(String name, Object value) {
                    }

                    @Override
                    public Object getAttribute(String name) {
                        return null;
                    }

                    @Override
                    public void removeAttribute(String name) {
                    }

                    @Override
                    public long getCreationTime() {
                        return System.currentTimeMillis();
                    }

                    @Override
                    public void invalidate() {
                    }
                };
            }
            return null;
        }

        @Override
        public Map<String, Session> getSessions() {
            return Map.of();
        }

        @Override
        public void removeSession(String sessionId) {
        }

        @Override
        public void invalidateExpiredSessions() {
        }

        @Override
        public void clearAllSessions() {
        }

        @Override
        public void invalidateSession(String sessionId) {

        }

        public void setValidSession(boolean validSession) {
            this.validSession = validSession;
        }
    }
}
