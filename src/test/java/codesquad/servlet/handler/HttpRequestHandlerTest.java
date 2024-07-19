package codesquad.servlet.handler;

import static codesquad.webserver.http.HttpMethod.GET;
import static codesquad.webserver.http.HttpVersion.HTTP1_1;
import static org.assertj.core.api.Assertions.assertThat;

import codesquad.configuration.TestDatabaseConfiguration;
import codesquad.domain.UserStorage;
import codesquad.domain.model.User;
import codesquad.helper.TestDatabaseExtension;
import codesquad.servlet.database.connection.DatabaseConnector;
import codesquad.servlet.database.connection.UserDao;
import codesquad.servlet.database.exception.InvalidDataAccessException;
import codesquad.servlet.execption.GlobalExceptionHandler;
import codesquad.servlet.fixture.UserFixture;
import codesquad.servlet.handler.resource.MappingMediaTypeFileExtensionResolver;
import codesquad.servlet.handler.resource.StaticResourceHandler;
import codesquad.servlet.handler.resource.StaticResourceReader;
import codesquad.webserver.http.HttpHeaders;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpPath;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpRequestBody;
import codesquad.webserver.http.HttpRequestLine;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.http.HttpStatus;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestDatabaseExtension.class)
class HttpRequestHandlerTest {

    HttpRequestHandler httpRequestHandler = new HttpRequestHandler(HandlerMapper.getInstance(),
            new StaticResourceHandler(StaticResourceReader.getInstance(), new MappingMediaTypeFileExtensionResolver()
            ),
            new GlobalExceptionHandler(StaticResourceReader.getInstance())
    );

    @Test
    @DisplayName("[Success] /index.html로 요청을 보내면 200 OK 응답이 발생한다.")
    void requestIndex() {
        HttpResponse httpResponse = HttpResponse.ok();
        httpRequestHandler.handle(new HttpRequest(
                new HttpRequestLine(GET, HttpPath.ofOnlyDefaultPath("/index.html"), HTTP1_1),
                HttpHeaders.empty(), null), new HttpResponse(null, HttpHeaders.empty(), null)
        );
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("[Success] /indexx.html로 요청을 보내면 400 응답이 발생한다.")
    void incorrectFileNameIndex() throws IOException {
        HttpResponse httpResponse = HttpResponse.ok();
        httpRequestHandler.handle(new HttpRequest(
                new HttpRequestLine(GET, HttpPath.ofOnlyDefaultPath("/indexx.html"), HTTP1_1),
                HttpHeaders.empty(), null), httpResponse
        );
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Nested
    @DisplayName("회원가입을 하는 기능은")
    @ExtendWith(TestDatabaseExtension.class)
    class Describe_Registration {

        DatabaseConnector databaseConnector = TestDatabaseConfiguration.getDatabaseConnector();

        UserStorage userStorage = new UserDao(databaseConnector);
        User user1;

        @BeforeEach
        void setUp() {
            user1 = UserFixture.createUser1();
        }

        @AfterEach
        void cleanUserDatabase() {
            try {
                userStorage.deleteById(user1.getId());
            } catch (InvalidDataAccessException e) {
                System.out.println("After Each Catch = " + e);
                // ignore
            }
        }

        @Test
        @DisplayName("[Fail] GET으로 회원가입은 404 실패한다.")
        void GET_registration_fail() {
            // given
            HttpRequest httpRequest = createRegistrationRequest(GET, null);
            HttpResponse httpResponse = HttpResponse.ok();

            // when
            httpRequestHandler.handle(httpRequest, httpResponse);

            // then
            HttpStatus httpStatus = httpResponse.getHttpStatus();
            assertThat(httpStatus).isEqualTo(HttpStatus.NOT_FOUND);
        }

        private HttpRequest createRegistrationRequest(HttpMethod httpMethod, Map<String, String> requestBodyParams) {
            HttpRequestLine httpRequestLine = new HttpRequestLine(httpMethod, HttpPath.of("/user/create", Map.of()),
                    HTTP1_1);
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

        private void cleanUsers() {
            user1 = userStorage.selectByUserId(user1.getUserId()).orElse(null);
            userStorage.deleteById(user1.getId());
        }

    }
}

