package codesquad.webserver;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.webserver.db.session.InMemorySessionManager;
import codesquad.webserver.db.session.SessionManager;
import codesquad.webserver.db.user.UserDatabase;
import codesquad.webserver.db.user.UserDatabaseFactory;
import codesquad.webserver.dispatcher.requesthandler.LoginRequestHandler;
import codesquad.webserver.dispatcher.requesthandler.UserCreateRequestHandler;
import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.model.User;
import codesquad.webserver.parser.RequestLine;
import codesquad.webserver.parser.enums.HttpMethod;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("사용자 인증 통합 테스트")
class UserAuthenticationIntegrationTest {

    private UserDatabase userDatabase;
    private SessionManager sessionManager;
    private UserCreateRequestHandler userCreateHandler;
    private LoginRequestHandler loginHandler;
    private FileReader fileReader;

    @BeforeEach
    void setUp() {
        userDatabase = UserDatabaseFactory.getInstance();
        sessionManager = InMemorySessionManager.getInstance();
        fileReader = new FileReader();
        userCreateHandler = new UserCreateRequestHandler(fileReader);
        loginHandler = new LoginRequestHandler(fileReader, userDatabase, sessionManager);
    }

    @AfterEach
    void tearDown() {
        userDatabase.clear();
        sessionManager.clearAllSessions();
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void testSuccessfulRegistration() {
        // Given
        HttpRequest registrationRequest = createRegistrationRequest("newuser", "password123", "New User");

        // When
        HttpResponse response = userCreateHandler.handle(registrationRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(302);
        assertThat(response.getHeaders().get("Location").get(0)).isEqualTo("/index.html");

        User user = userDatabase.findByUserId("newuser");
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo("newuser");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getName()).isEqualTo("New User");
    }

    @Test
    @DisplayName("로그인 성공 및 세션 생성 테스트")
    void testSuccessfulLoginAndSessionCreation() {
        // Given
        userDatabase.save(new User("testuser", "password123", "Test User"));
        HttpRequest loginRequest = createLoginRequest("testuser", "password123");

        // When
        HttpResponse response = loginHandler.handle(loginRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(302);
        assertThat(response.getHeaders().get("Location").get(0)).isEqualTo("/index.html");

        List<String> cookies = response.getHeaders().get("Set-Cookie");
        assertThat(cookies).isNotNull();

        String sessionId = cookies.get(0).split("=")[1];
        String path = cookies.get(1);
        assertThat(sessionId).isNotBlank();
        assertThat(path).isEqualTo("Path=/");

        User sessionUser = sessionManager.getSession(sessionId).getUser();
        assertThat(sessionUser).isNotNull();
        assertThat(sessionUser.getUserId()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 실패 테스트")
    void testLoginFailureWithWrongPassword() {
        // Given
        userDatabase.save(new User("testuser", "password123", "Test User"));
        HttpRequest loginRequest = createLoginRequest("testuser", "wrongpassword");

        // When
        HttpResponse response = loginHandler.handle(loginRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(302);
        assertThat(response.getHeaders().get("Location").get(0)).isEqualTo("/user/login_failed.html");
        assertThat(response.getHeaders().get("Set-Cookie")).isNull();
    }

    @Test
    @DisplayName("존재하지 않는 사용자 로그인 시도 테스트")
    void testLoginAttemptWithNonExistentUser() {
        // Given
        HttpRequest loginRequest = createLoginRequest("nonexistent", "password");

        // When
        HttpResponse response = loginHandler.handle(loginRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(302);
        assertThat(response.getHeaders().get("Location").get(0)).isEqualTo("/user/login_failed.html");
        assertThat(response.getHeaders().get("Set-Cookie")).isNull();
    }

    private HttpRequest createRegistrationRequest(String userId, String password, String name) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        String body = String.format("userId=%s&password=%s&name=%s", userId, password, name);
        RequestLine requestLine = new RequestLine(HttpMethod.POST, "/create", "/create", "HTTP/1.1");
        return new HttpRequest(requestLine, headers, new HashMap<>(), body);
    }

    private HttpRequest createLoginRequest(String username, String password) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        String body = String.format("username=%s&password=%s", username, password);
        RequestLine requestLine = new RequestLine(HttpMethod.POST, "/login", "/login", "HTTP/1.1");
        return new HttpRequest(requestLine, headers, new HashMap<>(), body);
    }
}