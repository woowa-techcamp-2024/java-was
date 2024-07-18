package codesquad.server.socket.read;

import java.io.*;

public class MessageReader {

    public static byte[] read(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] header = readHeader(inputStream);
        outputStream.write(header);

        int contentLength = getContentLength(new String(header));
        if (contentLength > 0) {
            byte[] body = readBody(inputStream, contentLength);
            outputStream.write(body);
        }

        return outputStream.toByteArray();
    }

    private static byte[] readHeader(InputStream inputStream) throws IOException {
        ByteArrayOutputStream headerStream = new ByteArrayOutputStream();
        int b;
        boolean eoh = false;
        while (!eoh) {
            b = inputStream.read();
            if (b == -1) break;
            headerStream.write(b);
            if (b == '\n' && headerStream.size() >= 4) {
                byte[] last4 = headerStream.toByteArray();
                int len = last4.length;
                if (last4[len-4] == '\r' && last4[len-3] == '\n' && last4[len-2] == '\r' && last4[len-1] == '\n') {
                    eoh = true;
                }
            }
        }
        return headerStream.toByteArray();
    }

    private static int getContentLength(String header) {
        for (String line : header.split("\r\n")) {
            if (line.startsWith("Content-Length:")) {
                return Integer.parseInt(line.split(":")[1].trim());
            }
        }
        return 0;
    }

    private static byte[] readBody(InputStream inputStream, int contentLength) throws IOException {
        byte[] body = new byte[contentLength];
        int bytesRead = 0;
        while (bytesRead < contentLength) {
            int result = inputStream.read(body, bytesRead, contentLength - bytesRead);
            if (result == -1) break;
            bytesRead += result;
        }
        return body;
    }
}