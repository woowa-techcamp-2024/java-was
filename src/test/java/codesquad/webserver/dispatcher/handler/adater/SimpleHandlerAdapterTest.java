package codesquad.webserver.dispatcher.handler.adater;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.webserver.dispatcher.requesthandler.RequestHandler;
import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.dispatcher.view.ModelKey;
import codesquad.webserver.dispatcher.view.ViewName;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.parser.RequestLine;
import codesquad.webserver.parser.enums.HttpMethod;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SimpleHandlerAdapterTest {

    private final SimpleHandlerAdapter adapter = new SimpleHandlerAdapter();

    private HttpRequest createTestHttpRequest() {
        RequestLine requestLine = new RequestLine(HttpMethod.GET, "/test", "/test", "HTTP/1.1");
        Map<String, List<String>> headers = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        String body = "";
        return new HttpRequest(requestLine, headers, params, body);
    }

    @Test
    @DisplayName("RequestHandler 인스턴스를 지원한다")
    void supportsRequestHandler() {
        // Given
        RequestHandler handler = request -> new ModelAndView(ViewName.TEMPLATE_VIEW);

        // When
        boolean result = adapter.supports(handler);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("RequestHandler가 아닌 인스턴스는 지원하지 않는다")
    void doesNotSupportNonRequestHandler() {
        // Given
        Object handler = new Object();

        // When
        boolean result = adapter.supports(handler);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("지원하는 핸들러를 정상적으로 처리한다")
    void handleSupportedHandler() {
        // Given
        HttpRequest testRequest = createTestHttpRequest();
        RequestHandler handler = request -> {
            ModelAndView mv = new ModelAndView(ViewName.TEMPLATE_VIEW);
            mv.addAttribute("testAttribute", "testValue");
            return mv;
        };

        // When
        ModelAndView result = adapter.handle(testRequest, handler);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getViewName()).isEqualTo(ViewName.TEMPLATE_VIEW);
        assertThat(result.getModel()).containsEntry("testAttribute", "testValue");
    }

    @Test
    @DisplayName("지원하지 않는 핸들러에 대해 예외 뷰를 반환한다")
    void handleUnsupportedHandler() {
        // Given
        HttpRequest testRequest = createTestHttpRequest();
        Object unsupportedHandler = new Object();

        // When
        ModelAndView result = adapter.handle(testRequest, unsupportedHandler);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getViewName()).isEqualTo(ViewName.EXCEPTION_VIEW);
        assertThat(result.getModel()).containsEntry(ModelKey.STATUS_CODE, 404);
    }
}