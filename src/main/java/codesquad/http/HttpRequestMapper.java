package codesquad.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestMapper {

    public static HttpRequest from(final BufferedReader bufferedReader) throws IOException {
        String requestLine;

        requestLine = bufferedReader.readLine();
        HttpRequestFirstLine firstLine = parseHttpRequestFirstLine(requestLine);
        HttpHeaders headers = parseHeaders(bufferedReader);

        return new HttpRequest(firstLine, headers);
    }

    private static HttpRequestFirstLine parseHttpRequestFirstLine(String requestLine) {
        String[] startLineParts = requestLine.split(" ");

        HttpMethod method = HttpMethod.valueOf(startLineParts[0]);
        String path = startLineParts[1];
        HttpVersion version = HttpVersion.of(startLineParts[2]);
        return new HttpRequestFirstLine(version, method, path);
    }

    private static HttpHeaders parseHeaders(BufferedReader bufferedReader) throws IOException {
        String requestLine;
        Map<String, String> headers = new HashMap<>();
        while (!(requestLine = bufferedReader.readLine()).isBlank()) {
            String[] headerParts = requestLine.split(": ", 2);
            headers.put(headerParts[0], headerParts[1]);
        }
        return HttpHeaders.of(headers);
    }

}
