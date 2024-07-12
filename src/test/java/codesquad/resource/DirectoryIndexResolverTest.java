package codesquad.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class DirectoryIndexResolverTest {

    DirectoryIndexResolver directoryIndexResolver;

    @BeforeEach
    void setUp() {
        directoryIndexResolver = DirectoryIndexResolver.getInstance();
    }

    @Nested
    class resolve_메서드는 {

        @Nested
        class request_uri가_디렉토리인_경우 {

            @Test
            void 디렉토리에_index_html이_존재하면_index_html을_반환한다() {
                Optional<Resource> foundResource = directoryIndexResolver.resolve("/test");
                assertAll(
                        () -> assertThat(foundResource).isPresent(),
                        () -> assertThat(foundResource.get().getFileName()).isEqualTo("index.html")
                );
            }

            @Test
            void index_html이_존재하지_않으면_빈_Optional을_반환한다() {
                Optional<Resource> foundResource = directoryIndexResolver.resolve("/empty");
                assertThat(foundResource).isEmpty();
            }
        }
    }
}