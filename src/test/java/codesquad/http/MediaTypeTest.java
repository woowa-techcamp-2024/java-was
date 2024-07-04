package codesquad.http;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MediaTypeTest {

    @Nested
    class find_메서드는 {

        @ParameterizedTest
        @CsvSource({
                "html, text/html",
                "css, text/css",
                "js, text/javascript",
                "svg, image/svg+xml",
                "png, image/png",
                "jpg, image/jpeg",
                "ico, image/vnd.microsoft.icon"
        })
        void 파일_확장자에_맞는_컨텐츠_타입을_반환한다(String fileExtension, String expectedMediaType) {
            Optional<MediaType> findMediaType = MediaType.find(fileExtension);
            assertThat(findMediaType).isNotEmpty();
            assertThat(findMediaType.get().getValue()).isEqualTo(expectedMediaType);
        }

        @Nested
        class 지원하지_않는_파일_형식의_경우 {

            @ParameterizedTest
            @ValueSource(strings = {"txt", "avi", "zzz", ""})
            void empty_optional을_반환한다(String fileExtension) {
                Optional<MediaType> findMediaType = MediaType.find(fileExtension);
                assertThat(findMediaType).isEmpty();
            }
        }
    }
}