package codesquad.infra;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class ImageWriter {

    private static final String IMAGE_DIRECTORY = System.getProperty("user.home") + "/images";

    public static String write(String imageName, byte[] imageBytes) {
        File dir = new File(IMAGE_DIRECTORY);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("[ERROR] directory 생성 실패: " + IMAGE_DIRECTORY);
        }

        String uniqueImageName = UUID.randomUUID() + "." + getExtension(imageName);
        File file = new File(IMAGE_DIRECTORY, uniqueImageName);

        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            outputStream.write(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return getImageUrl(uniqueImageName);
    }

    public static String getImageUrl(String fileName) {
        return IMAGE_DIRECTORY + "/" + fileName;
    }

    private static String getExtension(String imageName) {
        return imageName.split("\\.")[1];
    }
}
