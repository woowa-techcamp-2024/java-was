package util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class FilePathTest {

    @ParameterizedTest
    @CsvSource({
        "/path/to/file.txt, /path/to/file.txt",
        "/another/path/file.doc, /another/path/file.doc"
    })
    public void testGetPath(String input, String expected) {
        FilePath filePath = new FilePath(input);
        assertEquals(expected, filePath.getPath());
    }

    @ParameterizedTest
    @CsvSource({
        "/path/to/file.txt, file.txt",
        "/another/path/document.pdf, document.pdf",
        "/path/to/archive.tar.gz, archive.tar.gz",
        "/path/to/.hiddenfile, .hiddenfile",
        "/path/to/directory/, EMPTY"
    })
    public void testGetFileName(String input, String expected) {
        FilePath filePath = new FilePath(input);
        if(expected.equals("EMPTY")) {
            assertEquals("", filePath.getFileName());
            return;
        }
        assertEquals(expected, filePath.getFileName());
    }

    @ParameterizedTest
    @CsvSource({
        "/path/to, file.txt, /path/to/file.txt",
        "/path/to/, file.txt, /path/to/file.txt",
        "/another/path, document.pdf, /another/path/document.pdf"
    })
    public void testJoinPaths(String basePath, String addition, String expected) {
        FilePath filePath = new FilePath(basePath);
        FilePath joinedPath = filePath.join(addition);
        assertEquals(expected, joinedPath.getPath());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "/path/to/file.txt",
        "/another/path/document.pdf"
    })
    public void testConsistency(String path) {
        FilePath filePath = new FilePath(path);
        FilePath joinedPath = filePath.join("");
        assertEquals(filePath.getPath(), joinedPath.getPath());
    }
}
