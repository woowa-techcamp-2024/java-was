package codesquad.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class StaticResourceReader {

    public static final String STATIC_PATH = "src/main/resources/static/";

    public byte[] getFileContents(String path) throws IOException {
        String targetPath = STATIC_PATH + path;
        File file = new File(targetPath);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return fileInputStream.readAllBytes();
        }
    }
}
