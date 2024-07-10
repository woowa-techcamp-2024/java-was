package codesquad.webserver.parser;

import codesquad.webserver.http.HttpHeaders;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpPath;
import codesquad.webserver.http.HttpRequestBody;
import codesquad.webserver.http.HttpRequestLine;
import codesquad.webserver.http.HttpVersion;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {

    public HttpRequestLine parseHttpRequestFirstLine(BufferedReader bufferedReader) throws IOException {
        String requestLine = bufferedReader.readLine();
        String[] startLineParts = requestLine.split(" ");

        if (startLineParts.length != 3) {
            throw new IllegalArgumentException("Invalid request line: " + requestLine);
        }

        HttpMethod method = HttpMethod.valueOf(startLineParts[0]);
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
            String decodedQueryString = URLDecoder.decode(queryStringPart, "UTF-8");
            String[] queryStringComponentPart = decodedQueryString.split("=");
            allQueryStrings.put(queryStringComponentPart[0], queryStringComponentPart[1]);
        }

        HttpPath httpPath = HttpPath.of(defaultPath, allQueryStrings);
        return new HttpRequestLine(method, httpPath, version);
    }

    public HttpHeaders parseHeaders(BufferedReader bufferedReader) throws IOException {
        String requestLine;
        Map<String, String> headers = new HashMap<>();
        while ((requestLine = bufferedReader.readLine()) != null) {
            if (requestLine.isBlank()) {
                break;
            }
            String[] headerParts = requestLine.split(": ", 2);
            headers.put(headerParts[0], headerParts[1]);
        }
        return HttpHeaders.of(headers);
    }

    public HttpRequestBody parseBody(BufferedReader bufferedReader, int contentLength) throws IOException {
        if (contentLength <= 0) {
            return HttpRequestBody.ofEmpty();
        }

        char[] buffer = new char[contentLength];
        int bytesRead = bufferedReader.read(buffer, 0, contentLength);
        if (bytesRead != contentLength) {
            throw new IllegalArgumentException("Invalid content length: " + bytesRead);
        }

        StringBuilder stringBuilder = new StringBuilder();
        String bodyMessage = stringBuilder.append(buffer, 0, bytesRead).toString();
        String[] bodyParts = bodyMessage.split("&");

        Map<String, String> params = new HashMap<>();
        for (String bodyPart : bodyParts) {
            String[] paramNameAndValue = bodyPart.split("=");
            params.put(URLDecoder.decode(paramNameAndValue[0], "UTF-8"),
                    URLDecoder.decode(paramNameAndValue[1], "UTF-8"));
        }
        return new HttpRequestBody(params);
    }

}
