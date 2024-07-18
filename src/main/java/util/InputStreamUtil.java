package util;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamUtil {
    public static String readLineFromInputStream(InputStream inputStream) throws IOException {
        byte [] bytes = new byte[1024];
        int idx=0;
        int prev = -1;

        while (true) {
            int read = inputStream.read();
            if (read == -1) {
                return null;
            }

            if (prev == '\r' && read == '\n') {
                break;
            }

            bytes[idx] = (byte) read;
            prev = read;
            idx+=1;
        }

        return new String(bytes, 0, idx-1).strip();
    }
}
