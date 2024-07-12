package codesquad.handler;

import codesquad.http.HttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class HandlersMapperTest {

    HandlersMapper handlersMapper;

    @BeforeEach
    void setUp() {
        handlersMapper = new HandlersMapper();
    }

    @Nested
    class getRequestHandler_메서드는 {

        @ParameterizedTest
        @CsvSource({
                "/, DynamicResourceHandler",
                "/user/create, UserRegistrationHandler",
                "/user/login, UserLoginHandler",
        })
        void 처리_가능한_HTTP_request가_들어오면_대응되는_핸들러를_반환한다(String requestUri, String expectedRequestHandler) {
            HttpRequest httpRequest = new HttpRequest(requestUri, null, null, null, null, null);
            RequestHandler requestHandler = handlersMapper.getRequestHandler(httpRequest);

            assertThat(requestHandler).isNotNull();
            assertThat(requestHandler.getClass().getName()).endsWith(expectedRequestHandler);
        }

        @ParameterizedTest
        @ValueSource(strings = {"/test", "/nop", "/x", "//"})
        void 매핑되는_핸들러가_없는_경우_DynamicResourceHander를_반환한다(String requestUri) {
            HttpRequest httpRequest = new HttpRequest(requestUri, null, null, null, null, null);
            RequestHandler requestHandler = handlersMapper.getRequestHandler(httpRequest);

            assertThat(requestHandler).isNotNull();
            assertThat(requestHandler).isInstanceOf(DynamicResourceHandler.class);
        }
    }
}
