package handler;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.HeaderStringUtil.LOCATION;

import http.HttpRequest;
import http.HttpResponse;
import http.startline.RequestLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginPageHandlerTest {

    private LoginPageHandler loginPageHandler;
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    @BeforeEach
    void setUp() {
        loginPageHandler = new LoginPageHandler();
        RequestLine requestLine = RequestLine.fromString("GET /login HTTP/1.1");
        httpRequest = new HttpRequest(requestLine, null, null);
        httpResponse = HttpResponse.generateHttpResponse();
    }

    @Test
    void testLoginPageRedirection() {
        loginPageHandler.doGet(httpRequest, httpResponse);

        String redirectLocation = httpResponse.getHeader().getValue(LOCATION);
        assertTrue(redirectLocation.contains("login/index.html"), "Should redirect to login/index.html");
    }
}