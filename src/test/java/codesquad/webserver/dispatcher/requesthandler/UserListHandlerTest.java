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
import codesquad.webserver.session.InMemorySession;
import codesquad.webserver.session.Session;
import codesquad.webserver.session.SessionManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserListHandlerTest {

    private UserListHandler userListHandler;
    private FileReader mockFileReader;
    private UserDatabase mockUserDatabase;
    private SessionManager mockSessionManager;

    @BeforeEach
    void setUp() {
        mockFileReader = new MockFileReader();
        mockUserDatabase = new MockUserDatabase();
        mockSessionManager = new MockSessionManager();
        userListHandler = new UserListHandler(mockFileReader, mockUserDatabase, mockSessionManager);
    }

    @Test
    @DisplayName("로그인하지 않은 사용자의 요청을 처리한다")
    void handleGetForNonLoggedInUser() {
        HttpRequest request = createTestHttpRequest("/", null);

        ModelAndView result = userListHandler.handleGet(request);

        assertThat(result.getViewName()).isEqualTo(ViewName.TEMPLATE_VIEW);
        assertThat(result.getModel()).containsEntry(ModelKey.CONTENT, "mock user list content");
        assertThat(result.getModel()).containsEntry("isLoggedIn", false);
    }

    @Test
    @DisplayName("로그인한 사용자의 요청을 처리한다")
    void handleGetForLoggedInUser() {
        String sessionId = "testSessionId";
        HttpRequest request = createTestHttpRequest("/", sessionId);

        ModelAndView result = userListHandler.handleGet(request);

        assertThat(result.getViewName()).isEqualTo(ViewName.TEMPLATE_VIEW);
        assertThat(result.getModel()).containsEntry(ModelKey.CONTENT, "mock user list content");
        assertThat(result.getModel()).containsEntry("isLoggedIn", true);
        assertThat(result.getModel()).containsEntry("username", "Test User");
    }

    @Test
    @DisplayName("파일 읽기 실패 시 예외 뷰를 반환한다")
    void handleGetWithFileReadError() {
        mockFileReader = new MockFileReaderWithError();
        userListHandler = new UserListHandler(mockFileReader, mockUserDatabase, mockSessionManager);
        HttpRequest request = createTestHttpRequest("/", null);

        ModelAndView result = userListHandler.handleGet(request);

        assertThat(result.getViewName()).isEqualTo(ViewName.EXCEPTION_VIEW);
        assertThat(result.getModel()).containsEntry(ModelKey.STATUS_CODE, 500);
        assertThat(result.getModel()).containsEntry(ModelKey.ERROR_MESSAGE, "Internal server error");
    }

    private HttpRequest createTestHttpRequest(String path, String sessionId) {
        RequestLine requestLine = new RequestLine(HttpMethod.GET, path, path, "HTTP/1.1");
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
            return new FileResource(null, "user_list.html") {
                @Override
                public String readFileContent() {
                    return "mock user list content";
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
        public List<User> findAllUsers() {
            List<User> users = new ArrayList<>();
            users.add(new User("testUser1", "password1", "Test User 1"));
            users.add(new User("testUser2", "password2", "Test User 2"));
            return users;
        }

        @Override
        public void save(User user) {
        }

        @Override
        public Optional<User> findByUserId(String userId) {
            return null;
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
            return null;
        }

        @Override
        public Session getSession(String sessionId) {
            return new InMemorySession(sessionId, new User("testUser", "password", "Test User"));
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
