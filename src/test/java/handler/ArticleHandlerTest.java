package handler;

import http.HttpRequest;
import http.HttpResponse;
import http.startline.RequestLine;
import http.startline.UrlPath;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.UserRepository;
import session.Session;
import util.RequestContext;

class ArticleHandlerTest {

    private ArticleHandler articleHandler;
    private HttpResponse httpResponse;
    private Session session;
    private User user;
    RequestLine requestLine;
    private UserRepository userRepository = UserRepository.getInstance();

    @BeforeEach
    void setUp() {
        user = new User("userId", "password", "name", "email");
        userRepository.addUser(user);

        articleHandler = new ArticleHandler();
        requestLine = RequestLine.fromString("POST /user/create HTTP/1.1");
        httpResponse = HttpResponse.generateHttpResponse();
        session = new Session(1);
        session.setUser(user);

        RequestContext.of(new UrlPath("/user/create"), session);
    }

    @Test
    void testDoPostSuccess() {
        HttpRequest httpRequest = new HttpRequest(requestLine, null, null);
        httpRequest.setBody("title=TestTitle&content=TestContent".getBytes());

        articleHandler.doPost(httpRequest, httpResponse);
        // Assuming a way to verify the outcome without mocks, possibly by inspecting httpResponse's state
    }

    // Additional tests here
}