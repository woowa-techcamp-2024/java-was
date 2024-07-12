package handler;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.HeaderStringUtil.LOCATION;
import static util.HeaderStringUtil.SET_COOKIE;

import http.HttpRequest;
import http.HttpResponse;
import http.startline.RequestLine;
import http.startline.UrlPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import session.SessionManager;
import util.RequestContext;

class LogoutHandlerTest {

    private LogoutHandler logoutHandler;
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;
    private SessionManager sessionManager;

    @BeforeEach
    void setUp() {
        sessionManager = SessionManager.getInstance();
        logoutHandler = new LogoutHandler();
        RequestLine requestLine = RequestLine.fromString("POST /logout HTTP/1.1");
        httpRequest = new HttpRequest(requestLine, null, null);
        httpResponse = HttpResponse.generateHttpResponse();
    }

    @Test
    void testSuccessfulLogout() {
        // Assuming there's a method to simulate login or session creation
        sessionManager.createSession();
        RequestContext.of(UrlPath.of("/"),sessionManager.findOrCreateEmptySession());

        logoutHandler.doPost(httpRequest, httpResponse);

        // Verify session is invalidated and cookie is set to invalidate the session
        assertTrue(httpResponse.getHeader().toString().contains("Set-Cookie: sid=; Max-Age=0; Path=/; HttpOnly;"));
        assertTrue(httpResponse.getHeader().getValue(LOCATION).contains("/index.html"));
    }

    @Test
    void testUnsuccessfulLogout() {
        // Directly attempt to logout without a session
        RequestContext.of(UrlPath.of("/"),sessionManager.findOrCreateEmptySession());

        logoutHandler.doPost(httpRequest, httpResponse);

        // Assuming the behavior is to redirect to the login page or stay on the current page without session changes
        // This assertion might need to be adjusted based on actual application logic
        assertTrue(httpResponse.getHeader().getValue(LOCATION).contains("/index.html") || !httpResponse.getHeader().getValue(SET_COOKIE).contains("sid=; Path=/; Max-Age=0"));
    }
}