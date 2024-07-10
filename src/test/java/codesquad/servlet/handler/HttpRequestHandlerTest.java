package codesquad.servlet.handler;

import static codesquad.webserver.http.HttpMethod.GET;
import static codesquad.webserver.http.HttpMethod.POST;
import static codesquad.webserver.http.HttpVersion.HTTP1_1;
import static org.assertj.core.api.Assertions.assertThat;

import codesquad.servlet.handler.resource.MappingMediaTypeFileExtensionResolver;
import codesquad.servlet.handler.resource.StaticResourceHandler;
import codesquad.servlet.handler.resource.StaticResourceReader;
import codesquad.utils.FixedZonedDateTimeGenerator;
import codesquad.webserver.http.HttpHeaders;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpPath;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpRequestBody;
import codesquad.webserver.http.HttpRequestLine;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.http.HttpStatus;
import java.io.IOException;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HttpRequestHandlerTest {

    FixedZonedDateTimeGenerator fixedZonedDateTimeGenerator = new FixedZonedDateTimeGenerator(2000, 1, 1, 1, 1, 1, 1,
            ZoneOffset.UTC);

    HttpRequestHandler httpRequestHandler = new HttpRequestHandler(
            new HandlerMapper(),
            new StaticResourceHandler(
                    new StaticResourceReader(),
                    new MappingMediaTypeFileExtensionResolver()
            ),
            fixedZonedDateTimeGenerator
    );

    @Test
    @DisplayName("[Success] /index.html로 요청을 보내면 200 OK 응답이 발생한다.")
    void requestIndex() throws IOException {
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
    class Describe_Registration {

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

        @Test
        @DisplayName("[Success] POST로 회원가입은 성공하고 /index.html로 리다이렉트한다.")
        void POST_registration_success() {
            // given
            String userId = "아이디";
            String password = "비밀번호";
            String name = "이름";
            String email = "이메일@google.com";
            HttpRequest httpRequest = createRegistrationRequest(
                    POST,
                    createUserRegistrationRequestBodyParams(userId, password, name, email)
            );
            HttpResponse httpResponse = HttpResponse.ok();

            // when
            httpRequestHandler.handle(httpRequest, httpResponse);

            // then
            HttpStatus httpStatus = httpResponse.getHttpStatus();
            assertThat(httpStatus).isEqualTo(HttpStatus.FOUND);
            assertThat(httpResponse.getRedirect()).isPresent();
            assertThat(httpResponse.getRedirect().get()).isEqualTo("/index.html");
        }

        private HttpRequest createRegistrationRequest(HttpMethod httpMethod, Map<String, String> requestBodyParams) {
            HttpRequestLine httpRequestLine = new HttpRequestLine(httpMethod, HttpPath.of("/create", Map.of()),
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

    }
}