package codesquad.webserver.util;

import codesquad.webserver.util.FileReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileReaderTest {
    @Test
    public void hasFile(){
        assertTrue(FileReader.hasFile("/index.html"));
        assertFalse(FileReader.hasFile("/inx.html"));
    }

}
