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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserCreateRequestHandlerTest {

    private UserCreateRequestHandler userCreateRequestHandler;
    private UserDatabase mockUserDatabase;
    private FileReader mockFileReader;

    @BeforeEach
    void setUp() {
        mockFileReader = new MockFileReader();
        mockUserDatabase = new MockUserDatabase();
        userCreateRequestHandler = new UserCreateRequestHandler(mockFileReader, mockUserDatabase);
    }

    @Test
    @DisplayName("유효한 사용자 정보를 사용하여 POST 요청을 처리한다")
    void handlePostWithValidUser() {
        // Given
        HttpRequest request = createTestHttpRequest("userId=test&password=pass&name=Test");

        // When
        ModelAndView result = userCreateRequestHandler.handlePost(request);

        // Then
        assertThat(result.getViewName()).isEqualTo(ViewName.REDIRECT_VIEW);
        assertThat(result.getModel()).containsEntry(ModelKey.REDIRECT_URL, "/");
        assertThat(((MockUserDatabase) mockUserDatabase).isUserSaved()).isTrue();
    }

    @Test
    @DisplayName("중복된 사용자 정보를 사용하여 POST 요청을 처리한다")
    void handlePostWithDuplicateUser() {
        // Given
        ((MockUserDatabase) mockUserDatabase).setDuplicateUser(true);
        HttpRequest request = createTestHttpRequest("userId=test&password=pass&name=Test");

        // When
        ModelAndView result = userCreateRequestHandler.handlePost(request);

        // Then
        assertThat(result.getViewName()).isEqualTo(ViewName.REDIRECT_VIEW);
        assertThat(result.getModel()).containsEntry(ModelKey.REDIRECT_URL, "/");
        assertThat(((MockUserDatabase) mockUserDatabase).isUserSaved()).isFalse();
    }

    private HttpRequest createTestHttpRequest(String body) {
        RequestLine requestLine = new RequestLine(HttpMethod.POST, "/user/create", "/user/create", "HTTP/1.1");
        Map<String, List<String>> headers = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        return new HttpRequest(requestLine, headers, params, body);
    }

    private static class MockFileReader extends FileReader {
        @Override
        public FileResource read(String path) {
            return new FileResource(null, "index.html") {
                @Override
                public String readFileContent() {
                    return "mock file content";
                }
            };
        }
    }

    private static class MockUserDatabase implements UserDatabase {
        private boolean userSaved = false;
        private boolean duplicateUser = false;

        @Override
        public void save(User user) {
            if (duplicateUser) {
                throw new IllegalArgumentException("User with id " + user.getUserId() + " already exists");
            }
            userSaved = true;
        }

        @Override
        public Optional<User> findByUserId(String userId) {
            return null;
        }

        @Override
        public List<User> findAllUsers() {
            return null;
        }

        @Override
        public boolean existsByUserId(String userId) {
            return false;
        }

        @Override
        public void clear() {
        }

        public boolean isUserSaved() {
            return userSaved;
        }

        public void setDuplicateUser(boolean duplicateUser) {
            this.duplicateUser = duplicateUser;
        }
    }
}
