package codesquad.webserver.dispatcher.requesthandler;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.webserver.db.user.UserDatabase;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginFailHandlerTest {

    private LoginFailHandler loginFailHandler;
    private FileReader mockFileReader;
    private UserDatabase mockUserDatabase;
    private SessionManager mockSessionManager;

    @BeforeEach
    void setUp() {
        mockFileReader = new MockFileReader();
        mockUserDatabase = new MockUserDatabase();
        mockSessionManager = new MockSessionManager();
        loginFailHandler = new LoginFailHandler(mockFileReader, mockUserDatabase, mockSessionManager);
    }

    @Test
    @DisplayName("로그인 실패 시 로그인 실패 페이지를 반환한다")
    void handleGetLoginFailed() {
        // Given
        HttpRequest request = createTestHttpRequest("/login");

        // When
        ModelAndView result = loginFailHandler.handleGet(request);

        // Then
        assertThat(result.getViewName()).isEqualTo(ViewName.TEMPLATE_VIEW);
        assertThat(result.getModel()).containsEntry(ModelKey.CONTENT, "mock login fail content");
    }

    @Test
    @DisplayName("로그인 실패 페이지를 찾을 수 없을 때 예외 뷰를 반환한다")
    void handleGetLoginFailedWithFileNotFound() {
        // Given
        mockFileReader = new MockFileReaderWithError();
        loginFailHandler = new LoginFailHandler(mockFileReader, mockUserDatabase, mockSessionManager);
        HttpRequest request = createTestHttpRequest("/login");

        // When
        ModelAndView result = loginFailHandler.handleGet(request);

        // Then
        assertThat(result.getViewName()).isEqualTo(ViewName.TEMPLATE_VIEW);
        assertThat(result.getModel()).containsEntry(ModelKey.STATUS_CODE, 404);
        assertThat(result.getModel()).containsEntry(ModelKey.CONTENT, "Login page not found");
    }

    private HttpRequest createTestHttpRequest(String path) {
        RequestLine requestLine = new RequestLine(HttpMethod.GET, path, path, "HTTP/1.1");
        Map<String, List<String>> headers = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        String body = "";
        return new HttpRequest(requestLine, headers, params, body);
    }

    private static class MockFileReader extends FileReader {
        @Override
        public FileResource read(String path) {
            return new FileResource(null, "login_failed.html") {
                @Override
                public InputStream getInputStream() {
                    return new ByteArrayInputStream("mock login fail content".getBytes());
                }
            };
        }
    }

    private static class MockFileReaderWithError extends FileReader {
        @Override
        public FileResource read(String path) throws IOException {
            throw new IOException("File not found");
        }
    }

    private static class MockUserDatabase implements UserDatabase {
        @Override
        public void save(User user) {
        }

        @Override
        public Optional<User> findByUserId(String userId) {
            return null;
        }

        @Override
        public List<User> findAllUsers() {
            return List.of();
        }

        @Override
        public boolean existsByUserId(String userId) {
            return false;
        }

        @Override
        public void clear() {

        }

        public User findById(String userId) {
            return null;
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

        }
    }
}
