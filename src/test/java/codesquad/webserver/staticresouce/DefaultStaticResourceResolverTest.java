package codesquad.webserver.staticresouce;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultStaticResourceResolverTest {

    private DefaultStaticResourceResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new DefaultStaticResourceResolver();
    }

    @Test
    @DisplayName("HTML 파일 경로는 정적 리소스로 인식되지 않는다")
    void testIsNotStaticResourceForHtml() {
        String path = "/index.html";
        assertThat(resolver.isStaticResource(path)).isFalse();
    }

    @Test
    @DisplayName("지원되는 확장자를 가진 경로는 정적 리소스로 인식된다")
    void testIsStaticResourceForSupportedExtensions() {
        String path = "/style.css";
        assertThat(resolver.isStaticResource(path)).isTrue();

        path = "/script.js";
        assertThat(resolver.isStaticResource(path)).isTrue();
    }

    @Test
    @DisplayName("지원되지 않는 확장자를 가진 경로는 정적 리소스로 인식되지 않는다")
    void testIsNotStaticResourceForUnsupportedExtensions() {
        String path = "/file.unknown";
        assertThat(resolver.isStaticResource(path)).isFalse();
    }

    @Test
    @DisplayName("확장자가 없는 경로는 정적 리소스로 인식되지 않는다")
    void testIsNotStaticResourceForNoExtension() {
        String path = "/noextension";
        assertThat(resolver.isStaticResource(path)).isFalse();
    }

    @Test
    @DisplayName("대문자 확장자는 소문자로 변환되어 인식된다")
    void testIsStaticResourceForUppercaseExtension() {
        String path = "/image.JPG";
        assertThat(resolver.isStaticResource(path)).isTrue();
    }

    @Test
    @DisplayName("다양한 확장자에 대해 정적 리소스를 올바르게 인식한다")
    void testVariousExtensions() {
        assertThat(resolver.isStaticResource("/styles/style.css")).isTrue();
        assertThat(resolver.isStaticResource("/scripts/script.js")).isTrue();
        assertThat(resolver.isStaticResource("/images/image.jpg")).isTrue();
        assertThat(resolver.isStaticResource("/images/image.PNG")).isTrue();
        assertThat(resolver.isStaticResource("/images/image.GIF")).isTrue();
        assertThat(resolver.isStaticResource("/images/image.svg")).isTrue();
        assertThat(resolver.isStaticResource("/images/image.ico")).isTrue();
    }
}
