package codesquad.webserver.dispatcher.view.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import codesquad.webserver.dispatcher.view.ExceptionView;
import codesquad.webserver.dispatcher.view.JsonView;
import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.dispatcher.view.RedirectView;
import codesquad.webserver.dispatcher.view.TemplateView;
import codesquad.webserver.dispatcher.view.View;
import codesquad.webserver.dispatcher.view.ViewName;
import codesquad.webserver.template.SimpleTemplateEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultViewResolverTest {

    private DefaultViewResolver defaultViewResolver;

    @BeforeEach
    void setUp() {
        TemplateView templateView = new TemplateView(new SimpleTemplateEngine());
        ExceptionView exceptionView = new ExceptionView();
        JsonView jsonView = new JsonView();
        RedirectView redirectView = new RedirectView();

        defaultViewResolver = new DefaultViewResolver(templateView, exceptionView, jsonView, redirectView);
    }

    @Test
    @DisplayName("TEMPLATE_VIEW를 올바르게 반환한다")
    void resolveTemplateView() {
        ModelAndView modelAndView = new ModelAndView(ViewName.TEMPLATE_VIEW);
        View view = defaultViewResolver.resolveView(modelAndView);
        assertThat(view).isInstanceOf(TemplateView.class);
    }

    @Test
    @DisplayName("REDIRECT_VIEW를 올바르게 반환한다")
    void resolveRedirectView() {
        ModelAndView modelAndView = new ModelAndView(ViewName.REDIRECT_VIEW);
        View view = defaultViewResolver.resolveView(modelAndView);
        assertThat(view).isInstanceOf(RedirectView.class);
    }

    @Test
    @DisplayName("JSON_VIEW를 올바르게 반환한다")
    void resolveJsonView() {
        ModelAndView modelAndView = new ModelAndView(ViewName.JSON_VIEW);
        View view = defaultViewResolver.resolveView(modelAndView);
        assertThat(view).isInstanceOf(JsonView.class);
    }

    @Test
    @DisplayName("EXCEPTION_VIEW를 올바르게 반환한다")
    void resolveExceptionView() {
        ModelAndView modelAndView = new ModelAndView(ViewName.EXCEPTION_VIEW);
        View view = defaultViewResolver.resolveView(modelAndView);
        assertThat(view).isInstanceOf(ExceptionView.class);
    }

}
