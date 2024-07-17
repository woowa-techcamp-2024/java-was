package codesquad.webserver.filereader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileReaderTest {

    private FileReader fileReader;

    @BeforeEach
    void setUp() {
        fileReader = new FileReader();
    }

    @Test
    @DisplayName("기본 파일을 읽어들일 수 있다")
    void testReadDefaultFile() throws IOException {
        FileReader.FileResource fileResource = fileReader.read("/index.html");
        assertThat(fileResource).isNotNull();
        assertThat(fileResource.getFileName()).isEqualTo("index.html");
        assertThat(fileResource.readFileContent()).isNotEmpty();
    }

    @Test
    @DisplayName("존재하는 파일을 읽어들일 수 있다")
    void testReadExistingFile() throws IOException {
        // 실제로 존재하는 파일 경로로 테스트
        FileReader.FileResource fileResource = fileReader.read("/testfile.txt");
        assertThat(fileResource).isNotNull();
        assertThat(fileResource.getFileName()).isEqualTo("testfile.txt");
        assertThat(fileResource.readFileContent()).isEqualTo("This is a test file content.");
    }

    @Test
    @DisplayName("존재하지 않는 파일을 읽으려 하면 FileNotFoundException을 던진다")
    void testReadNonExistingFile() {
        assertThatThrownBy(() -> fileReader.read("/nonexistent.txt"))
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContaining("File not found");
    }

    @Test
    @DisplayName("폴더 하위 경로가 주어지면 index.html 파일을 읽어들일 수 있다")
    void testReadDirectoryIndexFile() throws IOException {
        // 실제로 존재하는 폴더 경로로 테스트
        FileReader.FileResource fileResource = fileReader.read("/folder/index.html");
        assertThat(fileResource).isNotNull();
        assertThat(fileResource.getFileName()).isEqualTo("index.html");
        assertThat(fileResource.readFileContent()).isEqualTo("Directory index file content.");
    }
}

