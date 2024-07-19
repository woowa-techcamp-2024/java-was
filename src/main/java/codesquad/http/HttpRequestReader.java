package codesquad.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class HttpRequestReader {

    private static HttpRequestReader instance;

    private HttpRequestReader() {
    }

    public static HttpRequestReader getInstance() {
        if (instance == null) {
            instance = new HttpRequestReader();
        }
        return instance;
    }

    public String readHeader(InputStream inputStream) throws IOException {
        ByteArrayOutputStream headerOutputStream = new ByteArrayOutputStream();

        int consecutiveNewlines = 0;
        int previousByte = -1;
        int currentByte;

        while ((currentByte = inputStream.read()) != -1) {
            headerOutputStream.write(currentByte);

            if (currentByte == '\n') {
                if (previousByte == '\r') {
                    consecutiveNewlines++;
                    if (consecutiveNewlines == 2) {
                        break;
                    }
                } else {
                    consecutiveNewlines = 0;
                }
            } else if (currentByte != '\r') {
                consecutiveNewlines = 0;
            }
            previousByte = currentByte;
        }

        if (consecutiveNewlines != 2) {
            String string = headerOutputStream.toString();
            System.out.println("string = " + string);
            throw new IOException("Header not properly terminated");
        }
        return headerOutputStream.toString();
    }

    public byte[] readBody(InputStream inputStream, int contentLength) throws IOException {
        byte[] body = new byte[contentLength];
        int bytesRead = 0;
        while (bytesRead < contentLength) {
            int read = inputStream.read(body, bytesRead, contentLength - bytesRead);
            if (read == -1) {
                throw new IOException("Unexpected end of stream while reading body");
            }
            bytesRead += read;
        }
        return body;
    }
}
