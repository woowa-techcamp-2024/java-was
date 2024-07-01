package codesquad.reader;

import java.io.FileInputStream;
import java.io.IOException;

public class FileByteReader {

    private static final String STATIC_RESOURCES_PATH = "src/main/resources/static";

    private final FileInputStream fileInputStream;

    public FileByteReader(String filePath) {
        try {
            fileInputStream = new FileInputStream(STATIC_RESOURCES_PATH + filePath);
        } catch (Exception e) {
            throw new IllegalArgumentException("파일을 찾을 수 없습니다.");
        }
    }

    public byte[] readAllBytes() {
        try {
            return fileInputStream.readAllBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException("파일을 읽을 수 없습니다.");
        }
    }
}
