package codesquad.webserver.dispatcher.view;

import codesquad.webserver.template.TemplateEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TemplateViewTest {

    private TemplateView templateView;
    private MockTemplateEngine mockTemplateEngine;

    @BeforeEach
    void setUp() {
        mockTemplateEngine = new MockTemplateEngine();
        templateView = new TemplateView(mockTemplateEngine);
    }

    @Test
    @DisplayName("템플릿을 정상적으로 렌더링한다")
    void renderTemplate() {
        // Given
        Map<String, Object> model = new HashMap<>();
        String templateContent = "Hello, {{name}}!";
        model.put(ModelKey.CONTENT, templateContent);
        model.put("name", "World");

        // When
        ViewResult result = templateView.render(model);

        // Then
        String expectedContent = "Hello, World!";
        assertThat(result.getBody()).isEqualTo(expectedContent.getBytes(StandardCharsets.UTF_8));
        assertThat(result.getHeaders()).isEmpty();
        assertThat(result.getCookies()).isEmpty();
        assertThat(result.getStatusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("모델에 템플릿 콘텐츠가 없는 경우 로그 에러를 기록한다")
    void testRenderWithMissingTemplateContent() {
        // Given
        Map<String, Object> model = new HashMap<>();

        // When
        ViewResult result = templateView.render(model);

        // Then
        assertThat(result.getStatusCode()).isEqualTo(200);  // StatusCode remains 200 even if content is missing
        assertThat(result.getBody()).isEmpty();
        assertThat(result.getHeaders()).isEmpty();
        assertThat(result.getCookies()).isEmpty();
    }

    private static class MockTemplateEngine implements TemplateEngine {
        @Override
        public String render(String template, Map<String, Object> model) {
            // 간단한 템플릿 엔진 구현
            if (template == null) {
                return "";
            }

            for (Map.Entry<String, Object> entry : model.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                template = template.replace("{{" + key + "}}", value.toString());
            }
            return template;
        }
    }
}