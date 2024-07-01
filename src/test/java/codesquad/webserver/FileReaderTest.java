package codesquad.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.Test;

public class FileReaderTest {


    // Successfully read the index.html file when requestPath is "/"
    @Test
    public void test_read_index_html_when_request_path_is_root() {
        // Given
        FileReader fileReader = new FileReader();
        String requestPath = "/";

        // When
        File file = fileReader.read(requestPath);

        // Then
        assertNotNull(file);
        assertTrue(file.exists());
        assertEquals("index.html", file.getName());
    }

    // Log an info message when a file is successfully read
    @Test
    public void test_log_info_message_when_file_successfully_read() {
        // Given
        FileReader fileReader = new FileReader();
        String requestPath = "/testfile.txt";

        // When
        File file = fileReader.read(requestPath);

        // Then
        assertNotNull(file);
        assertTrue(file.exists());
        // Note: Actual logging verification would require a logging framework or mock, which is not used here.
    }

    // Throw a RuntimeException when the file does not exist
    @Test
    public void test_throw_runtime_exception_when_file_does_not_exist() {
        // Given
        FileReader fileReader = new FileReader();
        String requestPath = "/nonexistentfile.txt";

        // When
        // Then
        assertThrows(RuntimeException.class, () -> {
            fileReader.read(requestPath);
        });
    }

    // Throw a RuntimeException when the file is a directory, not a regular file
    @Test
    public void test_throw_runtime_exception_when_file_is_directory() {
        // Given
        FileReader fileReader = new FileReader();
        String requestPath = "/directory";

        // When
        // Then
        // Expect RuntimeException to be thrown
        assertThrows(RuntimeException.class, () -> fileReader.read(requestPath));
    }

}