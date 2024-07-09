package codesquad.http;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequestReader {

    public String read(BufferedReader reader) throws IOException {
        StringBuilder request = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            request.append(line)
                    .append("\r\n");
        }
        return request.toString();
    }

    public String readBody(BufferedReader reader, int contentLength) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        if (contentLength > 0) {
            char[] body = new char[contentLength];
            reader.read(body);
            requestBody.append(new String(body));
        }
        return requestBody.toString();
    }

}
