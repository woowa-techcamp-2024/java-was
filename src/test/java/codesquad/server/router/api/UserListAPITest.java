package codesquad.server.router.api;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.constant.HttpMethod;
import codesquad.http.constant.HttpVersion;
import codesquad.http.element.RequestStartLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserListAPITest {
    private UserListAPI userListAPI;

    @BeforeEach
    void setUp() {
        userListAPI = new UserListAPI();
    }

    @Test
    @DisplayName("세션이 유효하면 /user/index.html로 리다이렉트해야 합니다")
    public void handleVerifiedTest() throws URISyntaxException {
        HttpRequest httpRequest = new HttpRequest(
                new RequestStartLine(HttpMethod.GET, new URI("/user/list"), HttpVersion.HTTP_1_1),
                null,
                null
        );

        // sessionDatabase.append(session.get(), new User(null));
        HttpResponse httpResponse = userListAPI.handle(httpRequest);
        assertEquals(httpResponse.getHttpHeaders().getHeader("Location").toString(), "/user/index.html");
    }

    @Test
    @DisplayName("세션이 없으면 로그인 페이지로 리다이렉트해야 합니다")
    public void handleNotVerifiedTest() throws URISyntaxException {
        HttpRequest httpRequest = new HttpRequest(
                new RequestStartLine(HttpMethod.GET, new URI("/user/list"), HttpVersion.HTTP_1_1),
                null,
                null
        );

        // sessionVerified.set(false);
        HttpResponse httpResponse = userListAPI.handle(httpRequest);
        assertEquals(httpResponse.getHttpHeaders().getHeader("Location").toString(), "/login/index.html");
    }
}
