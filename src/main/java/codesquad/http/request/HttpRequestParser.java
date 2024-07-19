package codesquad.http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {

    private final Logger log = LoggerFactory.getLogger(HttpRequestParser.class);

    public HttpRequest parse(InputStream inputStream) throws IOException {

        String line = readLine(inputStream);
        String[] requests;
        Map<String, String> headers = new HashMap<>();

//         Parse request line
        requests = line.split(" ");

//         Parse headers
        while (!(line = readLine(inputStream)).isEmpty()) {
            String[] headerParts = line.split(":", 2);
            if (headerParts.length == 2) {
                headers.put(headerParts[0].trim(), headerParts[1].trim());
            }
        }

//        Parse body
        String s = headers.get("Content-Length");
        byte[] body = new byte[0];

        if (s != null) {
            int length = Integer.parseInt(s);
            int bytesRead = 0;
            body = new byte[length];
            while (bytesRead < length) {
                int result = inputStream.read(body, bytesRead, length - bytesRead);
                if (result == -1) {
                    break;
                }
                bytesRead += result;
            }
//            log.info("body : {}", new String(body));
            return new HttpRequest(requests[0], requests[1], requests[2], headers, body);
        }
        return new HttpRequest(requests[0], requests[1], requests[2], headers, "".getBytes());
    }

    private static String readLine(InputStream inputStream) throws IOException {
        StringBuilder line = new StringBuilder();
        int nextChar;
        while ((nextChar = inputStream.read()) != -1) {
            if (nextChar == '\r') {
                nextChar = inputStream.read(); // read '\n'
                if (nextChar == '\n') {
                    break;
                }
            } else if (nextChar == '\n') {
                break;
            } else {
                line.append((char) nextChar);
            }
        }
        return line.toString();
    }

}
