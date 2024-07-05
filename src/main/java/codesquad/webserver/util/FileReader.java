package codesquad.webserver.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileReader {
    private FileReader() {
    }


    public static boolean hasFile(String path) {
        return FileReader.class.getResource("/static" + path) != null;
    }

    public static byte[] readFile(String path) throws IOException {
        try (InputStream inputStream = FileReader.class.getResourceAsStream("/static" + path)) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            return buffer.toByteArray();
        }
    }
}
