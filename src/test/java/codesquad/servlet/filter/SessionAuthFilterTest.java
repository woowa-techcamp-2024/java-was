package codesquad.servlet.filter;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.domain.model.User;
import codesquad.servlet.SessionStorage;
import codesquad.servlet.fixture.UserFixture;
import codesquad.webserver.http.HttpHeaders;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpPath;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpRequestBody;
import codesquad.webserver.http.HttpRequestLine;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.http.HttpStatus;
import codesquad.webserver.http.HttpVersion;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SessionAuthFilterTest {

    SessionStorage sessionStorage = SessionStorage.getInstance();
    SessionAuthFilter sessionAuthFilter = new SessionAuthFilter(SessionStorage.getInstance());
    User user = UserFixture.createUser1();
    String sessionId;

    @BeforeEach
    void initSession() {
        sessionId = sessionStorage.createSession(user);
    }

    @AfterEach
    void destroySession() {
        sessionStorage.deleteSession(sessionId);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/user/logout", "/api/user/info", "/api/user/list", "/article/write.html"})
    @DisplayName("[Success] 쿠키가 없다면 비인증 사용자이다.")
    void test1(String requestURI) {
        // given
        HttpRequest httpRequest = createHttpRequest(null, requestURI, null, Map.of("NotCookie", "123"));
        HttpResponse httpResponse = HttpResponse.ok();
        // when
        sessionAuthFilter.doFilter(httpRequest, httpResponse);
        // then
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/user/logout", "/api/user/info", "/api/user/list", "/article/write.html"})
    @DisplayName("[Success] 쿠키 중 세션관련 쿠키가 없으면 비인증 사용자이다.")
    void test5(String requestURI) {
        // given
        String notSession = "sssid";
        String wrongSessionId = sessionId + "123";
        HttpRequest httpRequest = createHttpRequest(null, requestURI, null,
                Map.of("Cookie", notSession + "=" + wrongSessionId));
        HttpResponse httpResponse = HttpResponse.ok();
        // when
        sessionAuthFilter.doFilter(httpRequest, httpResponse);
        // then
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
    }


    @ParameterizedTest
    @ValueSource(strings = {"/user/logout", "/api/user/info", "/api/user/list", "/article/write.html"})
    @DisplayName("[Success] 세션 쿠키의 값이 세션에 존재하지 않으면 비인증 사용자이다.")
    void test2(String requestURI) {
        // given
        String wrongSessionId = sessionId + "123";
        HttpRequest httpRequest = createHttpRequest(null, requestURI, null,
                Map.of("Cookie", "sid=" + wrongSessionId));
        HttpResponse httpResponse = HttpResponse.ok();
        // when
        sessionAuthFilter.doFilter(httpRequest, httpResponse);
        // then
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/user/logout", "/api/user/info", "/api/user/list", "/article/write.html"})
    @DisplayName("[Success] 세션 쿠키가 존재하고, 쿠키의 값이 세션에 존재하면 인증 사용자이다.")
    void test3(String requestURI) {
        // given
        HttpRequest httpRequest = createHttpRequest(null, requestURI, null, Map.of("Cookie", "sid=" + sessionId));
        HttpResponse httpResponse = HttpResponse.ok();
        // when
        sessionAuthFilter.doFilter(httpRequest, httpResponse);
        // then
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/", "/registration"})
    @DisplayName("[Success] 인증이 필요하지 않은 URI는 세션 인증 필터를 거치지 않는다.")
    void test4(String requestURI) {
        // given
        HttpRequest httpRequest = createHttpRequest(null, requestURI, null, null);
        HttpResponse httpResponse = HttpResponse.ok();
        // when
        sessionAuthFilter.doFilter(httpRequest, httpResponse);

        // then
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    private HttpRequest createHttpRequest(HttpMethod httpMethod,
                                          String requestUri,
                                          Map<String, String> queryString,
                                          Map<String, String> headers
    ) {
        HttpPath httpPath = createHttpPath(requestUri, queryString);
        HttpRequestLine httpRequestLine = createHttpRequestLine(httpMethod, httpPath);
        HttpHeaders httpHeaders = HttpHeaders.of(headers);
        HttpRequestBody httpRequestBody = HttpRequestBody.ofEmpty();

        return new HttpRequest(httpRequestLine, httpHeaders, httpRequestBody);
    }

    private HttpPath createHttpPath(String requestUri, Map<String, String> queryString) {
        return HttpPath.of(requestUri, queryString);
    }

    private HttpRequestLine createHttpRequestLine(HttpMethod httpMethod, HttpPath httpPath) {
        return new HttpRequestLine(httpMethod, httpPath, HttpVersion.HTTP1_1);
    }

}