package codesquad.webserver.dispatcher.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ModelAndViewTest {

    private ModelAndView modelAndView;

    @BeforeEach
    void setUp() {
        modelAndView = new ModelAndView(ViewName.TEMPLATE_VIEW);
    }

    @Test
    @DisplayName("뷰 이름을 올바르게 설정한다")
    void testViewName() {
        assertThat(modelAndView.getViewName()).isEqualTo(ViewName.TEMPLATE_VIEW);
    }

    @Test
    @DisplayName("모델에 속성을 추가하고 올바르게 반환한다")
    void testAddAttribute() {
        modelAndView.addAttribute("key1", "value1");
        modelAndView.addAttribute("key2", 123);

        Map<String, Object> model = modelAndView.getModel();
        assertThat(model).containsEntry("key1", "value1");
        assertThat(model).containsEntry("key2", 123);
    }

    @Test
    @DisplayName("모델이 불변인지 확인한다")
    void testModelIsImmutable() {
        modelAndView.addAttribute("key1", "value1");

        Map<String, Object> model = modelAndView.getModel();
        assertThat(model).containsEntry("key1", "value1");

        try {
            model.put("key2", "value2");
        } catch (UnsupportedOperationException e) {
            assertThat(e).isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
