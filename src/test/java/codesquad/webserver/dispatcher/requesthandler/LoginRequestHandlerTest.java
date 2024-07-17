package codesquad.webserver.dispatcher.requesthandler;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.webserver.db.user.User;
import codesquad.webserver.db.user.UserDatabase;
import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.dispatcher.view.ModelKey;
import codesquad.webserver.dispatcher.view.ViewName;
import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.parser.RequestLine;
import codesquad.webserver.parser.enums.HttpMethod;
import codesquad.webserver.session.Session;
import codesquad.webserver.session.SessionManager;
import codesquad.webserver.session.cookie.HttpCookie;
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

class LoginRequestHandlerTest {

    private LoginRequestHandler loginRequestHandler;
    private FileReader mockFileReader;
    private UserDatabase mockUserDatabase;
    private SessionManager mockSessionManager;

    @BeforeEach
    void setUp() {
        mockFileReader = new MockFileReader();
        mockUserDatabase = new MockUserDatabase();
        mockSessionManager = new MockSessionManager();
        loginRequestHandler = new LoginRequestHandler(mockFileReader, mockUserDatabase, mockSessionManager);
    }

    @Test
    @DisplayName("로그인 페이지를 성공적으로 로드한다")
    void handleGetLoginPage() {
        // Given
        HttpRequest request = createTestHttpRequest("/login", HttpMethod.GET, null);

        // When
        ModelAndView result = loginRequestHandler.handleGet(request);

        // Then
        assertThat(result.getViewName()).isEqualTo(ViewName.TEMPLATE_VIEW);
        assertThat(result.getModel()).containsEntry(ModelKey.CONTENT, "mock login page content");
    }

    @Test
    @DisplayName("로그인 페이지를 찾을 수 없을 때 예외 뷰를 반환한다")
    void handleGetLoginPageNotFound() {
        // Given
        mockFileReader = new MockFileReaderWithError();
        loginRequestHandler = new LoginRequestHandler(mockFileReader, mockUserDatabase, mockSessionManager);
        HttpRequest request = createTestHttpRequest("/login", HttpMethod.GET, null);

        // When
        ModelAndView result = loginRequestHandler.handleGet(request);

        // Then
        assertThat(result.getViewName()).isEqualTo(ViewName.TEMPLATE_VIEW);
        assertThat(result.getModel()).containsEntry(ModelKey.STATUS_CODE, 404);
        assertThat(result.getModel()).containsEntry(ModelKey.CONTENT, "Login page not found");
    }

    @Test
    @DisplayName("올바른 자격 증명으로 로그인 성공")
    void handlePostLoginSuccess() {
        // Given
        String requestBody = "username=testUser&password=password";
        HttpRequest request = createTestHttpRequest("/login", HttpMethod.POST, requestBody);

        // When
        ModelAndView result = loginRequestHandler.handlePost(request);

        // Then
        assertThat(result.getViewName()).isEqualTo(ViewName.REDIRECT_VIEW);
        assertThat(result.getModel()).containsEntry(ModelKey.REDIRECT_URL, "/");
        assertThat(result.getModel()).containsKey(ModelKey.COOKIES);
        List<HttpCookie> cookies = (List<HttpCookie>) result.getModel().get(ModelKey.COOKIES);
        assertThat(cookies).hasSize(1);
        assertThat(cookies.get(0).getName()).isEqualTo("SID");
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 실패")
    void handlePostLoginFailureDueToWrongPassword() {
        // Given
        String requestBody = "username=testUser&password=wrongPassword";
        HttpRequest request = createTestHttpRequest("/login", HttpMethod.POST, requestBody);

        // When
        ModelAndView result = loginRequestHandler.handlePost(request);

        // Then
        assertThat(result.getViewName()).isEqualTo(ViewName.REDIRECT_VIEW);
        assertThat(result.getModel()).containsEntry(ModelKey.REDIRECT_URL, "/login/login_failed");
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 로그인 실패")
    void handlePostLoginFailureDueToNonExistentUser() {
        // Given
        String requestBody = "username=nonExistentUser&password=password";
        HttpRequest request = createTestHttpRequest("/login", HttpMethod.POST, requestBody);

        // When
        ModelAndView result = loginRequestHandler.handlePost(request);

        // Then
        assertThat(result.getViewName()).isEqualTo(ViewName.REDIRECT_VIEW);
        assertThat(result.getModel()).containsEntry(ModelKey.REDIRECT_URL, "/login/login_failed");
    }

    private HttpRequest createTestHttpRequest(String path, HttpMethod method, String body) {
        RequestLine requestLine = new RequestLine(method, path, path, "HTTP/1.1");
        Map<String, List<String>> headers = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        return new HttpRequest(requestLine, headers, params, body);
    }

    private static class MockFileReader extends FileReader {
        @Override
        public FileResource read(String path) {
            return new FileResource(null, "index.html") {
                @Override
                public InputStream getInputStream() {
                    return new ByteArrayInputStream("mock login page content".getBytes());
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
            if ("testUser".equals(userId)) {
                return Optional.of(new User("testUser", "password", "Test User"));
            } else {
                throw new IllegalArgumentException("User not found");
            }
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
    }

    private static class MockSessionManager implements SessionManager {
        @Override
        public Session createSession(User user) {
            return new Session() {
                @Override
                public String getId() {
                    return "testSessionId";
                }

                @Override
                public User getUser() {
                    return user;
                }

                @Override
                public long getCreationTime() {
                    return System.currentTimeMillis();
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
                public void invalidate() {
                }
            };
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
