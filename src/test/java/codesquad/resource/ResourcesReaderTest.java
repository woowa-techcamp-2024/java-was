package codesquad.resource;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ResourcesReaderTest {

    @Nested
    class readResource_메서드는 {

        @Test
        void 정적_리소스를_읽어_Reosurce_객체를_생성해_반환한다() {
            Optional<Resource> readResource = ResourcesReader.readResource("/test/index.html");
            assertAll(
                    () -> assertThat(readResource).isPresent(),
                    () -> assertThat(readResource.get().getFileName()).isEqualTo("index.html"),
                    () -> assertThat(readResource.get().getExtension()).isEqualTo("html"),
                    () -> assertThat(readResource.get().isFile()).isTrue()
            );
        }

        @Test
        void 정적_리소스가_존재하지_않으면_빈_Optional을_반환한다() {
            Optional<Resource> readResource = ResourcesReader.readResource("/non-exist.html");
            assertThat(readResource).isEmpty();
        }
    }
}