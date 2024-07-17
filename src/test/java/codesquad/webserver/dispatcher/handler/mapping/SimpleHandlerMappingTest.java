package codesquad.webserver.dispatcher.handler.mapping;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.parser.RequestLine;
import codesquad.webserver.parser.enums.HttpMethod;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SimpleHandlerMappingTest {

    private SimpleHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new SimpleHandlerMapping();
    }

    @Test
    @DisplayName("핸들러를 추가하고 가져올 수 있다")
    void testAddAndGetHandler() {
        String path = "/test";
        Object handler = new Object();

        handlerMapping.addHandler(path, handler);
        Object retrievedHandler = handlerMapping.getHandler(createTestHttpRequest(path));

        assertThat(retrievedHandler).isEqualTo(handler);
    }

    @Test
    @DisplayName("리플렉션을 사용하여 프라이빗 필드에 접근하고 수정한다")
    void testReflectionAccessAndModifyPrivateField() throws NoSuchFieldException, IllegalAccessException {
        // 리플렉션을 사용하여 handlers 필드에 접근
        Field handlersField = SimpleHandlerMapping.class.getDeclaredField("handlers");
        handlersField.setAccessible(true);

        // 새로운 핸들러 맵 생성
        Map<String, Object> newHandlers = new HashMap<>();
        String path = "/newHandler";
        Object handler = new Object();
        newHandlers.put(path, handler);

        // handlers 필드를 새로운 맵으로 설정
        handlersField.set(handlerMapping, newHandlers);

        // 수정된 handlers 필드 값을 가져와서 검증
        Map<String, Object> modifiedHandlers = (Map<String, Object>) handlersField.get(handlerMapping);
        assertThat(modifiedHandlers).isEqualTo(newHandlers);
        assertThat(modifiedHandlers.get(path)).isEqualTo(handler);

        // 새로운 핸들러를 정상적으로 가져올 수 있는지 검증
        Object retrievedHandler = handlerMapping.getHandler(createTestHttpRequest(path));
        assertThat(retrievedHandler).isEqualTo(handler);
    }

    private HttpRequest createTestHttpRequest(String path) {
        RequestLine requestLine = new RequestLine(HttpMethod.GET, path, path, "HTTP/1.1");
        Map<String, List<String>> headers = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        String body = "";
        return new HttpRequest(requestLine, headers, params, body);
    }
}
