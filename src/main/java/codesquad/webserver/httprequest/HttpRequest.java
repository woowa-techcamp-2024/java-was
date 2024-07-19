package codesquad.webserver.httprequest;


import codesquad.webserver.parser.RequestLine;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpRequest {

    private RequestLine requestLine;
    private Map<String, List<String>> headers;
    private Map<String, String> params;
    private String body = "";
    private Map<String, List<String>> multipartFields = new HashMap<>();
    ;
    private Map<String, List<FileItem>> multipartFiles = new HashMap<>();
    ;

    public HttpRequest() {
    }

    public HttpRequest(RequestLine requestLine, Map<String, List<String>> headers, Map<String, String> params,
                       String body) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.params = params;
        this.body = body;
    }

    public HttpRequest(RequestLine requestLine, Map<String, List<String>> headers, Map<String, String> params,
                       String body,
                       Map<String, List<String>> multipartFields, Map<String, List<FileItem>> multipartFiles) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.params = params;
        this.body = body;
        this.multipartFields = multipartFields;
        this.multipartFiles = multipartFiles;
    }

    private static final String SESSION_KEY = "SID";

    public String getSessionIdFromRequest() {
        List<String> cookie = this.headers.get("Cookie");
        if (cookie == null || cookie.isEmpty()) {
            return "";
        }

        Optional<String> sessionId = cookie.stream()
                .flatMap(c -> List.of(c.split(";")).stream())
                .map(String::trim)
                .filter(c -> c.startsWith(SESSION_KEY + "="))
                .map(c -> c.substring((SESSION_KEY + "=").length()))
                .findFirst();

        if (sessionId.isEmpty()) {
            return "";
        }

        return sessionId.get();
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public Map<String, List<String>> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public Map<String, String> getParams() {
        return Collections.unmodifiableMap(params);
    }

    public String getBody() {
        return body;
    }

    public HttpRequest setRequestLine(RequestLine requestLine) {
        this.requestLine = requestLine;
        return this;
    }

    public HttpRequest setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
        return this;
    }

    public HttpRequest setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public HttpRequest setBody(String body) {
        this.body = body;
        return this;
    }

    public Map<String, List<String>> getMultipartFields() {
        return multipartFields;
    }

    public void setMultipartFields(Map<String, List<String>> multipartFields) {
        this.multipartFields = multipartFields;
    }

    public Map<String, List<FileItem>> getMultipartFiles() {
        return multipartFiles;
    }

    public void setMultipartFiles(
            Map<String, List<FileItem>> multipartFiles) {
        this.multipartFiles = multipartFiles;
    }

    @Override
    public String toString() {
        System.out.println("리퀘스트 헤더" + headers);
        StringBuilder httpRequestString = new StringBuilder();

        httpRequestString.append(requestLine.toString()).append("\r\n");

        headers.forEach((key, values) -> values.forEach(
                value -> httpRequestString.append(key).append(": ").append(value).append("\r\n")));

        httpRequestString.append("\r\n");

        if (body != null && !body.isEmpty()) {
            httpRequestString.append(body);
        }

        return httpRequestString.toString();
    }

    public static class FileItem {
        public final String filename;
        public final byte[] content;

        public FileItem(String filename, byte[] content) {
            this.filename = filename;
            this.content = content;
        }

        public String getFilename() {
            return filename;
        }

        public byte[] getContent() {
            return content;
        }

        @Override
        public String toString() {
            return "FileItem{" +
                    "filename='" + filename +
                    '}';
        }
    }
}
