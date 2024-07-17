package codesquad.webserver.dispatcher.requesthandler;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.dispatcher.view.ModelKey;
import codesquad.webserver.dispatcher.view.ViewName;
import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.db.user.User;
import codesquad.webserver.parser.RequestLine;
import codesquad.webserver.parser.enums.HttpMethod;
import codesquad.webserver.session.Session;
import codesquad.webserver.session.SessionManager;
import codesquad.webserver.session.cookie.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LogoutRequestHandlerTest {

    private LogoutRequestHandler logoutRequestHandler;
    private FileReader mockFileReader;
    private SessionManager mockSessionManager;

    @BeforeEach
    void setUp() {
        mockFileReader = new MockFileReader();
        mockSessionManager = new MockSessionManager();
        logoutRequestHandler = new LogoutRequestHandler(mockFileReader, mockSessionManager);
    }

    @Test
    @DisplayName("로그아웃 요청을 처리한다")
    void handlePostLogout() {
        // Given
        String sessionId = "testSessionId";
        HttpRequest request = createTestHttpRequest("/logout", HttpMethod.POST, sessionId);

        // When
        ModelAndView result = logoutRequestHandler.handlePost(request);

        // Then
        assertThat(result.getViewName()).isEqualTo(ViewName.REDIRECT_VIEW);
        assertThat(result.getModel()).containsEntry(ModelKey.REDIRECT_URL, "/");
        List<HttpCookie> cookies = (List<HttpCookie>) result.getModel().get(ModelKey.COOKIES);
        assertThat(cookies).hasSize(1);
        assertThat(cookies.get(0).getName()).isEqualTo("SID");
        assertThat(cookies.get(0).getValue()).isEqualTo("");
        assertThat(cookies.get(0).getMaxAge()).isEqualTo(0);
    }

    @Test
    @DisplayName("세션이 없을 때 예외 처리")
    void handlePostSessionNotFound() {
        // Given
        HttpRequest request = createTestHttpRequest("/logout", HttpMethod.POST, null);

        // When
        ModelAndView result = logoutRequestHandler.handlePost(request);

        // Then
        assertThat(result.getViewName()).isEqualTo(ViewName.EXCEPTION_VIEW);
        assertThat(result.getModel()).containsEntry(ModelKey.STATUS_CODE, 404);
        assertThat(result.getModel()).containsEntry(ModelKey.ERROR_MESSAGE, "Session not found");
    }

    private HttpRequest createTestHttpRequest(String path, HttpMethod method, String sessionId) {
        RequestLine requestLine = new RequestLine(method, path, path, "HTTP/1.1");
        Map<String, List<String>> headers = new HashMap<>();
        if (sessionId != null) {
            headers.put("Cookie", List.of("JSESSIONID=" + sessionId));
        }
        Map<String, String> params = new HashMap<>();
        String body = "";
        return new HttpRequest(requestLine, headers, params, body) {
            @Override
            public String getSessionIdFromRequest() {
                return sessionId;
            }
        };
    }

    private static class MockFileReader extends FileReader {
        @Override
        public FileResource read(String path) {
            return new FileResource(null, "index.html");
        }
    }

    private static class MockSessionManager implements SessionManager {
        @Override
        public Session createSession(User user) {
            return null;
        }

        @Override
        public Session getSession(String sessionId) {
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
            if (sessionId == null) {
                throw new IllegalStateException("Session not found");
            }
        }
    }
}
