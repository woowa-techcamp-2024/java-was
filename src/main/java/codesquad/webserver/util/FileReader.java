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

    public static String readFileToString(String path) throws IOException {
        InputStream inputStream = FileReader.class.getResourceAsStream("/static" + path);
        if (inputStream == null) {
            throw new IOException("File Not Found: " + path);
        }
        return inputStreamToString(inputStream);
    }

    public static byte[] stringToByteArray(String input) throws IOException {
        return input.getBytes("UTF-8");
    }

    private static String inputStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
}
