package codesquad.http.request;

import java.io.*;

public class HttpRequestParser {

    public static HttpRequest parseHttpRequest(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        HttpRequest request = new HttpRequest();

        // Parse request line
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IOException("Empty request line");
        }
        String[] requestLineParts = requestLine.split(" ");
        if (requestLineParts.length != 3) {
            throw new IOException("Invalid request line: " + requestLine);
        }
        request.setMethod(requestLineParts[0]);
        request.setUrl(requestLineParts[1]);
        request.setVersion(requestLineParts[2]);

        // Parse headers
        String headerLine;
        while (!(headerLine = reader.readLine()).isEmpty()) {
            String[] headerParts = headerLine.split(": ", 2);
            if (headerParts.length == 2) {
                request.addHeader(headerParts[0], headerParts[1]);
            }
        }

        // Parse body (if any)
        StringBuilder body = new StringBuilder();
        while (reader.ready()) {
            body.append((char) reader.read());
        }
        request.setBody(body.toString());

        return request;
    }

    public static void main(String[] args) {
        // Example usage
        try {
            // Mock InputStream (replace with actual socket input stream in real usage)
            String httpRequestString = "GET /index.html HTTP/1.1\r\n" +
                    "Host: www.example.com\r\n" +
                    "Connection: keep-alive\r\n" +
                    "\r\n";
            InputStream inputStream = new ByteArrayInputStream(httpRequestString.getBytes());
            HttpRequest request = parseHttpRequest(inputStream);
            System.out.println(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
