package codesquad.handler;

import codesquad.http.HttpHeaders;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.MediaType;
import codesquad.http.StatusCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class UserRegistrationHandlerTest {

    UserRegistrationHandler userRegistrationHandler;

    @BeforeEach
    void setUp() {
        userRegistrationHandler = UserRegistrationHandler.getInstance();
    }

    @Nested
    class handle_메서드는 {

        @ParameterizedTest
        @ValueSource(strings = {"GET", "PUT", "DELETE"})
        void POST_메서드_요청이_아니면_405_응답을_반환한다(String httpMethod) {
            HttpRequest httpRequest = new HttpRequest(null, httpMethod, null, null, null, null);
            HttpResponse response = userRegistrationHandler.handle(httpRequest);
            StatusCode statusCode = response.statusCode();
            assertThat(statusCode).isEqualTo(StatusCode.METHOD_NOT_ALLOWED);
        }

        @Test
        void content_type이_x_www_form_urlencoded가_아니면_400_응답을_반환한다() {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.addValue(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue());
            HttpRequest httpRequest = new HttpRequest(null, "POST", null, null, httpHeaders, null);
            HttpResponse httpResponse = userRegistrationHandler.handle(httpRequest);
            assertThat(httpResponse.statusCode()).isEqualTo(StatusCode.BAD_REQUEST);
        }

        @Test
        void request_body가_없으면_400_응답을_반환한다() {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.addValue(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED.getValue());
            HttpRequest httpRequest = new HttpRequest(null, "POST", null, null, httpHeaders, "");
            HttpResponse httpResponse = userRegistrationHandler.handle(httpRequest);
            assertThat(httpResponse.statusCode()).isEqualTo(StatusCode.BAD_REQUEST);
        }
    }

}