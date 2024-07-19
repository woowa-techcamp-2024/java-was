package codesquad.resource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageReader {

    public static Resource read(String fileName) {
        String directory = System.getProperty("user.home") + "/images";
        File file = new File(directory, fileName);
        byte[] content = new byte[(int) file.length()];

        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            inputStream.read(content);
        } catch (IOException e) {
            return null;
        }
        return Resource.file(fileName, content);
    }
}
