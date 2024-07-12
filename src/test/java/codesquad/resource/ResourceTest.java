package codesquad.resource;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ResourceTest {

    @Nested
    class file_메서드는 {

        @Test
        void 파일_리소스_객체를_생성한다() {
            Resource resource = Resource.file("test.txt", "test".getBytes());
            assertAll(
                    () -> assertThat(resource.getFileName()).isEqualTo("test.txt"),
                    () -> assertThat(resource.getExtension()).isEqualTo("txt"),
                    () -> assertThat(resource.getContent()).isEqualTo("test".getBytes()),
                    () -> assertThat(resource.isFile()).isTrue()
            );
        }
    }

    @Nested
    class directory_메서드는 {

        @Test
        void 디렉토리_리소스_객체를_생성한다() {
            Resource resource = Resource.directory("test");
            assertAll(
                    () -> assertThat(resource.getFileName()).isEqualTo("test"),
                    () -> assertThat(resource.getExtension()).isNull(),
                    () -> assertThat(resource.getContent()).isNull(),
                    () -> assertThat(resource.isFile()).isFalse()
            );
        }
    }
}