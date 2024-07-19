package codesquad.utils;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    private FileUtil() {
    }

    public static void createFile(String path) {
        File file = new File(path);
        File parentDir = file.getParentFile();

        try {
            createParentDir(parentDir);
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createParentDir(File parentDir) throws IOException {
        if (parentDir != null && !parentDir.exists()) {
            boolean dirCreated = parentDir.mkdirs();
            if (!dirCreated) {
                throw new IOException();
            }
        }
    }
}
