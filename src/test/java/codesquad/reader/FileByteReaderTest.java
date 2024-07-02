package codesquad.reader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileByteReaderTest {

    @Test
    @DisplayName("파일을 읽어온다.")
    void readAllBytesSuccess() {
        // Arrange
        FileByteReader fileByteReader = new FileByteReader("/index.html");
        // Act
        var actualResult = fileByteReader.readAllBytes();
        // Assert
        assertThat(actualResult).isNotEmpty();
    }

    @Test
    @DisplayName("파일을 찾을 수 없을때 오류를 반환한다.")
    void readAllBytesFailWhenFileNotFound() {
        // Act & Assert
        assertThatThrownBy(() -> new FileByteReader("/index2.html"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("파일을 찾을 수 없습니다.");
    }

}