package codesquad.servlet.handler.api;

import static codesquad.webserver.http.HttpMethod.POST;
import static codesquad.webserver.http.HttpVersion.HTTP1_1;
import static org.assertj.core.api.Assertions.assertThat;

import codesquad.domain.InMemoryUserStorage;
import codesquad.domain.model.User;
import codesquad.servlet.fixture.UserFixture;
import codesquad.webserver.http.HttpHeaders;
import codesquad.webserver.http.HttpPath;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpRequestBody;
import codesquad.webserver.http.HttpRequestLine;
import codesquad.webserver.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UserRegistrationHandlerTest {

    InMemoryUserStorage inMemoryUserStorage = InMemoryUserStorage.getInstance();
    UserRegistrationHandler userRegistrationHandler = new UserRegistrationHandler(inMemoryUserStorage);

    User user1;

    @BeforeEach
    void setUp() {
        user1 = UserFixture.createUser1();
    }

    @AfterEach
    void clean() {
        user1 = inMemoryUserStorage.selectByUserId(user1.getUserId()).orElse(null);
        inMemoryUserStorage.deleteById(user1.getId());
    }

    @Nested
    @DisplayName("회원가입 API는")
    class Describe_UserRegistrationAPISpecTest {

        @Test
        @DisplayName("[Success] 성공 시 /index.html로 리다이렉트 응답을 내려준다.")
        void redirectSpec() {
            // given
            HttpRequest httpRequest = createRegistrationRequest(
                    createUserRegistrationRequestBodyParams(user1.getUserId(), user1.getPassword(), user1.getName(),
                            user1.getEmail())
            );
            HttpResponse httpResponse = HttpResponse.ok();

            // when
            userRegistrationHandler.service(httpRequest, httpResponse);

            // then
            assertThat(httpResponse.getRedirect()).isPresent();
            assertThat(httpResponse.getRedirect().get()).isEqualTo("/index.html");
        }

        private HttpRequest createRegistrationRequest(Map<String, String> requestBodyParams) {
            HttpRequestLine httpRequestLine = new HttpRequestLine(POST, HttpPath.of("/user/create", Map.of()), HTTP1_1);
            HttpHeaders httpHeaders = HttpHeaders.empty();
            HttpRequestBody httpRequestBody = new HttpRequestBody(requestBodyParams);
            return new HttpRequest(httpRequestLine, httpHeaders, httpRequestBody);
        }

        private Map<String, String> createUserRegistrationRequestBodyParams(String userId, String password,
                                                                            String name, String email
        ) {
            HashMap<String, String> requestBodyParams = new HashMap<>();
            requestBodyParams.put("userId", userId);
            requestBodyParams.put("password", password);
            requestBodyParams.put("name", name);
            requestBodyParams.put("email", email);
            return requestBodyParams;
        }

    }
}