package handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import http.HttpRequest;
import http.HttpResponse;
import http.startline.RequestLine;
import http.startline.ResponseLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.UserRepository;
import service.UserService;

class UserCreateHandlerTest {

    private UserCreateHandler userCreateHandler;
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    @BeforeEach
    void setUp() {
        UserService userService = new UserService(UserRepository.getInstance());
        userCreateHandler = new UserCreateHandler(userService);
        RequestLine requestLine = RequestLine.fromString("POST /user/create HTTP/1.1");
        httpRequest = new HttpRequest(requestLine, null, null);
        httpResponse = HttpResponse.generateHttpResponse();
    }

    @Test
    void testUserCreationSuccess() {
        // Simulate setting request body with valid user details
        httpRequest.setBody("userId=testUser&password=testPass&name=testUser&email=".getBytes());

        userCreateHandler.doPost(httpRequest, httpResponse);

        // Assuming the success is indicated by a specific status code or message
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        assertEquals(302, responseLine.getStatusCode());
    }

    @Test
    void testUserCreationFailure() {
        // Simulate setting request body with invalid user details
        httpRequest.setBody("username=testUser".getBytes()); // Missing password

        userCreateHandler.doPost(httpRequest, httpResponse);

        // Assuming the failure is indicated by a specific status code or error message
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        assertEquals(302, responseLine.getStatusCode());
    }
}