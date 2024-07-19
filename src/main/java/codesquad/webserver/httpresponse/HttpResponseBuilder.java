package codesquad.webserver.httpresponse;

import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.session.cookie.HttpCookie;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponseBuilder {
    private static final Logger log = LoggerFactory.getLogger(HttpResponseBuilder.class);
    public static final Map<String, String> MIME_TYPES;

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
        mimeTypes.put("txt", "text/plain");
        MIME_TYPES = Collections.unmodifiableMap(mimeTypes);
    }

    private int statusCode;
    private String statusMessage;
    private final List<HttpResponse.Header> headers;
    private final List<HttpCookie> cookies;
    private byte[] body;

    public HttpResponseBuilder() {
        this.statusCode = 200;
        this.statusMessage = "OK";
        this.headers = new ArrayList<>();
        this.cookies = new ArrayList<>();
        this.body = new byte[0];
    }

    public HttpResponseBuilder statusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public HttpResponseBuilder statusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        return this;
    }

    public HttpResponseBuilder header(String name, String value) {
        this.headers.add(new HttpResponse.Header(name, value));
        return this;
    }

    public HttpResponseBuilder headers(Map<String, String> headers) {
        headers.forEach(this::header);
        return this;
    }

    public HttpResponseBuilder cookie(HttpCookie cookie) {
        this.cookies.add(cookie);
        return this;
    }

    public HttpResponseBuilder cookies(List<HttpCookie> cookies) {
        this.cookies.addAll(cookies);
        return this;
    }

    public HttpResponseBuilder body(byte[] body) {
        this.body = body;
        return this;
    }

    public HttpResponse build() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(statusCode);
        response.setStatusMessage(statusMessage);
        headers.forEach(header -> response.addHeader(header.getName(), header.getValue()));
        cookies.forEach(response::addCookie);
        response.setBody(body);
        return response;
    }

    public static HttpResponseBuilder ok() {
        return new HttpResponseBuilder().statusCode(200).statusMessage("OK");
    }

    public static HttpResponseBuilder notFound() {
        return new HttpResponseBuilder().statusCode(404).statusMessage("Not Found");
    }

    public static HttpResponse buildNotFoundFromFile() {
        try {
            return buildFromFile(new FileReader().read("/error/404.html"), 404, "NotFound");
        } catch (IOException e) {
            return notFound()
                    .body("NotFound".getBytes())
                    .build();
        }
    }

    public static HttpResponseBuilder forbidden() {
        return new HttpResponseBuilder()
                .statusCode(403)
                .statusMessage("Forbidden")
                .header("Content-Type", "text/html");
    }

    public static HttpResponse buildForbiddenFromFile() {
        try {
            return buildFromFile(new FileReader().read("/error/403.html"), 403, "Forbidden");
        } catch (IOException e) {
            return forbidden()
                    .body("Forbidden".getBytes())
                    .build();
        }
    }

    public static HttpResponseBuilder redirect(String location) {
        return new HttpResponseBuilder()
                .statusCode(302)
                .statusMessage("Found")
                .header("Location", location);
    }

    public static HttpResponseBuilder methodNotAllowed() {
        return new HttpResponseBuilder().statusCode(405).statusMessage("Method Not Allowed");
    }

    public static HttpResponse buildMethodNotAllowedFromFile() {
        try {
            return buildFromFile(new FileReader().read("/error/405.html"), 405, "Method Not Allowed");
        } catch (IOException e) {
            return methodNotAllowed()
                    .body("Method Not Allowed".getBytes())
                    .build();
        }
    }

    public static HttpResponseBuilder serverError() {
        return new HttpResponseBuilder().statusCode(500).statusMessage("Internal Server Error");
    }

    public static HttpResponse buildServerErrorFromFile() {
        try {
            return buildFromFile(new FileReader().read("/error/500.html"), 500, "Internal Server Error");
        } catch (IOException e) {
            return serverError()
                    .body("ServerError".getBytes())
                    .build();
        }
    }

    public static HttpResponse buildFromFile(FileReader.FileResource resource, int statusCode, String statusMessage)
            throws IOException {
        byte[] body = readAllBytes(resource.getInputStream());
        String contentType = getContentType(resource.getFileName());

        return new HttpResponseBuilder()
                .statusCode(statusCode)
                .statusMessage(statusMessage)
                .header("Content-Type", contentType)
                .header("Content-Length", String.valueOf(body.length))
                .body(body)
                .build();
    }

    public static HttpResponse buildFromFile(FileReader.FileResource resource) throws IOException {
        byte[] body = readAllBytes(resource.getInputStream());
        String contentType = getContentType(resource.getFileName());

        return ok()
                .header("Content-Type", contentType)
                .header("Content-Length", String.valueOf(body.length))
                .body(body)
                .build();
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
