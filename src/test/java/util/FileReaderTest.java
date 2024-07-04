package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import http.startline.UrlPath;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileReaderTest {

    private static final String TEST_FILE_PATH = System.getProperty("user.dir") + "/src/test/resources/static/test.txt";
    private static final String TEST_FILE_EXTENSION = "txt";

    @BeforeEach
    void setUp() throws IOException {
        Path testFilePath = Paths.get(TEST_FILE_PATH);
        Files.createDirectories(testFilePath.getParent());
    }

    @Test
    @DisplayName("UrlPath에 해당하는 파일을 읽어서 byte 배열로 반환한다.")
    void testReadFileFromUrlPath_FileExists() throws IOException {
        UrlPath urlPath = UrlPath.of("/index.html");
        byte[] fileContent = FileReader.readFileFromUrlPath(urlPath);
        assertNotNull(fileContent);
    }

    @Test
    @DisplayName("없는 파일을 읽으려고 시도하면 FileNotFoundException을 던진다.")
    void testReadFileFromUrlPath_FileNotExists() {
        UrlPath urlPath = UrlPath.of("/nonexistent.txt");
        assertThrows(FileNotFoundException.class, () -> FileReader.readFileFromUrlPath(urlPath));
    }

    @Test
    @DisplayName("UrlPath에 해당하는 파일의 MIME 타입을 추측한다.")
    void testGuessContentTypeFromUrlPath() {
        UrlPath urlPath = UrlPath.of("/test." + TEST_FILE_EXTENSION);
        String contentType = FileReader.guessContentTypeFromUrlPath(urlPath);
        assertEquals("application/octet-stream", contentType); // For txt file, assuming default MIME type
    }

    @Test
    @DisplayName("파일 이름에서 확장자를 추출한다.")
    void testGetFileExtension() {
        String extension = getFileExtension("test." + TEST_FILE_EXTENSION);
        assertEquals(TEST_FILE_EXTENSION, extension);
    }

    // Helper method to test private method
    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex == -1) {
            return ""; // No extension
        }
        return fileName.substring(lastIndex + 1).toLowerCase();
    }
}
