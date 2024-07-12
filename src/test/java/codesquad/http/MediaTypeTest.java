package codesquad.http;

import codesquad.error.HttpStatusException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MediaTypeTest {

    @Nested
    class find_메서드는 {

        @ParameterizedTest
        @CsvSource({
                "html, text/html; charset=utf-8",
                "css, text/css",
                "js, text/javascript",
                "svg, image/svg+xml",
                "png, image/png",
                "jpg, image/jpeg",
                "ico, image/vnd.microsoft.icon"
        })
        void 파일_확장자에_맞는_컨텐츠_타입을_반환한다(String fileExtension, String expectedMediaType) {
            MediaType findMediaType = MediaType.find(fileExtension);
            assertThat(findMediaType).isNotNull();
            assertThat(findMediaType.getValue()).isEqualTo(expectedMediaType);
        }

        @Nested
        class 지원하지_않는_파일_형식의_경우 {

            @ParameterizedTest
            @ValueSource(strings = {"avi", "zzz", ""})
            void empty_optional을_반환한다(String fileExtension) {
                assertThatThrownBy(() -> MediaType.find(fileExtension))
                        .isInstanceOf(HttpStatusException.class)
                        .hasMessageContaining("[ERROR] 지원하지 않는 파일 형식입니다.");
            }
        }
    }
}