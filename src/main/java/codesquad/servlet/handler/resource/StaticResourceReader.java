package codesquad.servlet.handler.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class StaticResourceReader {

    public static final String STATIC_PATH = "static";

    public byte[] getFileContents(String path) throws IOException {
        String targetPath = STATIC_PATH + path;
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(targetPath)) {
            if (resourceAsStream == null) {
                throw new FileNotFoundException();
            }
            return resourceAsStream.readAllBytes();
        }
    }
}
