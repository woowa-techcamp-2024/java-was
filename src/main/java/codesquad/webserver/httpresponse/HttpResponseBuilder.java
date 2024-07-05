package codesquad.webserver.httpresponse;

import codesquad.webserver.filereader.FileReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpResponseBuilder {
    private static final Map<String, String> MIME_TYPES = new HashMap<>();
    static {
        MIME_TYPES.put("html", "text/html");
        MIME_TYPES.put("css", "text/css");
        MIME_TYPES.put("js", "application/javascript");
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("svg", "image/svg+xml");
        MIME_TYPES.put("ico", "image/x-icon");
    }

    public static HttpResponse build(FileReader.FileResource fileName) throws IOException {
        byte[] body = readAllBytes(fileName.getInputStream());
        String contentType = getContentType(fileName.getFileName());

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", contentType);
        headers.put("Content-Length", String.valueOf(body.length));

        return new HttpResponse(200, "OK", headers, body);
    }

    public static HttpResponse buildNotFoundResponse() {
        String body = "<html><body><h1>Not Found</h1></body></html>";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        headers.put("Content-Length", String.valueOf(body.length()));

        return new HttpResponse(404, "Not Found", headers, body.getBytes());
    }

    public static HttpResponse buildMethodErrorResponse() {
        return new HttpResponse(405, "Method Not Allowed");
    }

    public static HttpResponse buildServerErrorResponse() {
        return new HttpResponse(500, "Internal Server Error");
    }

    public static HttpResponse buildRedirectResponse(String location) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", location);
        return new HttpResponse(302, "Found", headers, new byte[0]);
    }

    private static String getContentType(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            String extension = fileName.substring(dotIndex + 1).toLowerCase();
            return MIME_TYPES.getOrDefault(extension, "application/octet-stream");
        }
        return "application/octet-stream";
    }

    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }
}