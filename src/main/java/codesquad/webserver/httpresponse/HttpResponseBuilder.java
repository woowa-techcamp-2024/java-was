package codesquad.webserver.httpresponse;

import codesquad.webserver.filereader.FileReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HttpResponseBuilder {
    public static final Map<String, String> MIME_TYPES;
    private static final Logger log = LoggerFactory.getLogger(HttpResponseBuilder.class);

    static {
        Map<String, String> mimeTypes = new HashMap<>();
        mimeTypes.put("html", "text/html");
        mimeTypes.put("css", "text/css");
        mimeTypes.put("js", "application/javascript");
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("svg", "image/svg+xml");
        mimeTypes.put("ico", "image/x-icon");

        MIME_TYPES = Collections.unmodifiableMap(mimeTypes);
    }

    public static HttpResponse build(int statusCode, String statusMessage, Map<String, List<String>> headers,
                                     byte[] body) {
        HttpResponse.Builder builder = HttpResponse.builder()
                .statusCode(statusCode)
                .statusMessage(statusMessage);

        if (headers != null) {
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
        }

        if (body != null) {
            builder.body(body);
        }

        return builder.build();
    }

    public static HttpResponse build(FileReader.FileResource fileName) throws IOException {
        byte[] body = readAllBytes(fileName.getInputStream());
        String contentType = getContentType(fileName.getFileName());

        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", Collections.singletonList(contentType));
        headers.put("Content-Length", Collections.singletonList(String.valueOf(body.length)));

        return build(200, "OK", headers, body);
    }

    public static HttpResponse buildNotFoundResponse() {
        FileReader fileReader = new FileReader();
        byte[] body;
        try {
            body = readAllBytes(fileReader.read("/404.html").getInputStream());
        } catch (Exception e) {
            body = "<html><body>NOT FOUND</body></html>".getBytes();
        }
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", Collections.singletonList("text/html"));
        headers.put("Content-Length", Collections.singletonList(String.valueOf(body.length)));

        return build(404, "Not Found", headers, body);
    }

    public static HttpResponse buildMethodErrorResponse() {
        return build(405, "Method Not Allowed", Collections.emptyMap(), null);
    }

    public static HttpResponse buildServerErrorResponse() {
        return build(500, "Internal Server Error", Collections.emptyMap(), null);
    }

    public static HttpResponse buildRedirectResponse(String location) {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Location", Collections.singletonList(location));
        return build(302, "Found", headers, null);
    }

    public static HttpResponse buildRedirectResponse(String location, Map<String, List<String>> inputHeaders) {
        Map<String, List<String>> headers = new HashMap<>();
        headers.putAll(inputHeaders);
        headers.put("Location", Collections.singletonList(location));
        return build(302, "Found", headers, null);
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