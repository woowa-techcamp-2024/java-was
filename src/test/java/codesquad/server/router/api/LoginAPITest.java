package codesquad.server.router.api;

import codesquad.domain.entity.User;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.constant.HttpMethod;
import codesquad.http.constant.HttpVersion;
import codesquad.http.element.RequestBody;
import codesquad.http.element.RequestStartLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static codesquad.Main.userDatabase;
import static org.junit.jupiter.api.Assertions.*;

public class LoginAPITest {
    private final LoginAPI loginAPI = new LoginAPI();

    @BeforeEach
    public void setUp() {
        userDatabase.insert(new User("userId1", "nickname1", "password1"));
        userDatabase.insert(new User("userId2", "nickname2", "password2"));
    }

    @Test
    @DisplayName("등록되어 있는 사용자는 login이 가능해야 합니다")
    public void loginRegisterUser() throws URISyntaxException {
        RequestStartLine requestStartLine = new RequestStartLine(HttpMethod.POST, new URI("/login"), HttpVersion.HTTP_1_1);
        HttpRequest httpRequest = new HttpRequest(requestStartLine, null, new RequestBody("userId=userId1&password=password1"));

        HttpResponse httpResponse = loginAPI.handle(httpRequest);
    }

    @Test
    @DisplayName("등록되어 있지 않은 사용자는 login이 불가능해야 합니다")
    public void loginNotRegisterUser() throws URISyntaxException {
        RequestStartLine requestStartLine = new RequestStartLine(HttpMethod.POST, new URI("/login"), HttpVersion.HTTP_1_1);
        HttpRequest httpRequest = new HttpRequest(requestStartLine, null, new RequestBody("userId=userId1&password=password2"));

        HttpResponse httpResponse = loginAPI.handle(httpRequest);
        assertEquals(httpResponse.getHttpHeaders().getHeader("Location").toString(), "/login/login_failed.html");
        assertNull(httpResponse.getHttpHeaders().getHeader("Set-Cookie"));
    }
}
