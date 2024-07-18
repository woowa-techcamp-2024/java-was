package handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import http.Header;
import http.HttpRequest;
import http.HttpResponse;
import http.startline.RequestLine;
import http.startline.ResponseLine;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.UserRepository;
import session.Session;
import session.SessionManager;
import util.RequestContext;

class UserListHandlerTest {

    private UserListHandler userListHandler;
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;
    private Session session;
    private User user;

    @BeforeEach
    void setUp() {
        userListHandler = new UserListHandler();
        // sid 설정
        session = SessionManager.getInstance().createSession(); // 세션 1번 추가
        RequestLine requestLine = RequestLine.fromString("GET /user/list HTTP/1.1");
        Header header = Header.emptyHeader();
        user = new User("testUser", "testPass", "testUser", "");
        httpRequest = new HttpRequest(requestLine, header, null);
        httpResponse = HttpResponse.generateHttpResponse();
    }

    @Test
    void testUserListRetrievalSuccess() {
        // header sid=1
        UserRepository.getInstance().addUser(user);
        SessionManager.getInstance().getSession(session.getSessionId());

        session.setAttribute(Session.USER, user);
        RequestContext.of(((RequestLine)httpRequest.getStartLine()).getUrlPath(), session);
        // Req

        // Simulate a GET request for retrieving the user list
        userListHandler.doGet(httpRequest, httpResponse);

        // Assuming the success is indicated by a specific status code or the presence of user data in the body
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        assertEquals(200, responseLine.getStatusCode());
        System.out.println(new String(httpResponse.getBody()));
        assertTrue(new String(httpResponse.getBody()).contains("testUser"));
    }

    @Test
    void testUserListRetrievalWithNoUsers() {
        Session session = SessionManager.getInstance().getSession(2);
        RequestContext.of(((RequestLine)httpRequest.getStartLine()).getUrlPath(), session);

        // Ensure no users are available for this test case
        SessionManager.getInstance().invalidateSession(1);
        // Simulate a GET request for retrieving the user list
        userListHandler.doGet(httpRequest, httpResponse);

        // Assuming the appropriate handling is indicated by a specific status code or message
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        assertEquals(302, responseLine.getStatusCode());
        assertEquals("/login/index.html", httpResponse.getHeader().getValue("Location"));
    }
}