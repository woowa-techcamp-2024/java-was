package codesquad.webserver.parser;

import codesquad.webserver.http.HttpHeaders;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpPath;
import codesquad.webserver.http.HttpRequestLine;
import codesquad.webserver.http.HttpVersion;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {

    private static final String UTF_8 = "UTF-8";

    public HttpRequestLine parseHttpRequestFirstLine(InputStream inputStream) throws IOException {
        String requestLine = readLine(inputStream);
        if (requestLine == null) {
            throw new IllegalArgumentException("Invalid request line: " + requestLine);
        }

        String[] startLineParts = requestLine.split(" ");

        if (startLineParts.length != 3) {
            throw new IllegalArgumentException("Invalid request line: " + requestLine);
        }

        HttpMethod method = HttpMethod.fromString(startLineParts[0]);
        String defaultPath = startLineParts[1];
        HttpVersion version = HttpVersion.of(startLineParts[2]);

        String[] pathParts = defaultPath.split("\\?");
        if (pathParts.length == 1) {
            HttpPath httpPath = HttpPath.ofOnlyDefaultPath(pathParts[0]);
            return new HttpRequestLine(method, httpPath, version);
        }

        defaultPath = pathParts[0];
        String queryStrings = pathParts[1];

        String[] queryStringParts = queryStrings.split("&");
        Map<String, String> allQueryStrings = new HashMap<>();
        for (String queryStringPart : queryStringParts) {
            String decodedQueryString = URLDecoder.decode(queryStringPart, UTF_8);
            String[] queryStringComponentPart = decodedQueryString.split("=");
            allQueryStrings.put(queryStringComponentPart[0], queryStringComponentPart[1]);
        }

        HttpPath httpPath = HttpPath.of(defaultPath, allQueryStrings);
        return new HttpRequestLine(method, httpPath, version);
    }

    public HttpHeaders parseHeaders(InputStream inputStream) throws IOException {
        String headerLine;
        Map<String, String> headers = new HashMap<>();
        while (!(headerLine = readLine(inputStream)).isEmpty()) {
            String[] headerParts = headerLine.split(": ", 2);
            if (headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
            }
        }
        return HttpHeaders.of(headers);
    }

    public Map<String, String> parseBody(byte[] body) throws IOException {
        String bodyMessage = new String(body, UTF_8);
        String[] bodyParts = bodyMessage.split("&");

        Map<String, String> params = new HashMap<>();
        for (String bodyPart : bodyParts) {
            String[] paramNameAndValue = bodyPart.split("=", 2);
            if (paramNameAndValue.length == 2) {
                params.put(URLDecoder.decode(paramNameAndValue[0], UTF_8),
                        URLDecoder.decode(paramNameAndValue[1], UTF_8));
            }
        }
        return params;
    }

    public byte[] readBody(InputStream inputStream, int contentLength) throws IOException {
        if (contentLength <= 0) {
            return new byte[0];
        }
        byte[] buffer = new byte[contentLength];
        int bytesRead = 0;
        int totalBytesRead = 0;

        while (totalBytesRead < contentLength
                && (bytesRead = inputStream.read(buffer, totalBytesRead, contentLength - totalBytesRead)) != -1) {
            totalBytesRead += bytesRead;
        }

        if (totalBytesRead != contentLength) {
            throw new IllegalArgumentException("Invalid content length: " + bytesRead);
        }
        return buffer;
    }

    private String readLine(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int currentByte;
        while ((currentByte = inputStream.read()) != -1) {
            if (currentByte == '\n' || currentByte == '\r') {
                if (currentByte == '\r') {
                    inputStream.mark(1);
                    int nextByte = inputStream.read();
                    if (nextByte != '\n') {
                        inputStream.reset();
                    }
                }
                break;
            }
            baos.write(currentByte);
        }
        String line = baos.toString(UTF_8);
        return line.isEmpty() && currentByte == -1 ? null : line;
    }

}

