package codesquad.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileReader {
    public static byte[] readFile(String path) throws IOException {
        String STATIC_DIRECTORY_PATH = "./src/main/resources/static";
        File file = new File(STATIC_DIRECTORY_PATH +path);

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] content = new byte[(int) file.length()];
        int bytesRead = fileInputStream.read(content);
        if(bytesRead != file.length()){
            throw new IOException("Could not read file");
        }
        fileInputStream.close();
        return content;
    }
}
