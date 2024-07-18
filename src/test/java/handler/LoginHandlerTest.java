package handler;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.HeaderStringUtil.LOCATION;

import http.Header;
import http.HttpRequest;
import http.HttpResponse;
import http.startline.RequestLine;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.UserRepositoryMemory;
import service.UserService;
import session.SessionManager;

class LoginHandlerTest {

    private LoginHandler loginHandler;
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;
    private UserService userService;
    private SessionManager sessionManager;
    private UserRepositoryMemory userRepositoryMemory;

    @BeforeEach
    void setUp() throws SQLException {
        userRepositoryMemory = UserRepositoryMemory.getInstance();
        userService = new UserService(userRepositoryMemory);
        sessionManager = SessionManager.getInstance();
        loginHandler = new LoginHandler();
        // Simplified HttpRequest and HttpResponse setup for testing
        RequestLine requestLine = RequestLine.fromString("POST /login HTTP/1.1");
        Header header = Header.emptyHeader();
        byte[] body = "username=userId&password=password".getBytes(StandardCharsets.UTF_8);
        httpRequest = new HttpRequest(requestLine, header, body);
        httpResponse = HttpResponse.generateHttpResponse();
    }

    @Test
    void successfulLogin() throws SQLException {
        userRepositoryMemory.addUser(new User("userId", "password", "name", "email"));
        loginHandler.doPost(httpRequest, httpResponse);

        // Check for redirection by examining the "Location" header
        assertTrue(httpResponse.getHeader().getValue(LOCATION).contains("/index.html"));
        assertTrue(httpResponse.getHeader().toString().contains("sid="));
    }

    // Additional tests for failed login scenarios can be implemented similarly
}