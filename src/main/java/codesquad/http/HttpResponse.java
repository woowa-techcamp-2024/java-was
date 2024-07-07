package codesquad.http;

public class HttpResponse {

    public static final String CRLF = "\r\n";
    private final StatusCode statusCode;
    private final String httpVersion;
    private final HttpHeaders headers;
    private final byte[] body;

    public HttpResponse(StatusCode statusCode, String httpVersion, HttpHeaders headers, byte[] body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.httpVersion = httpVersion;
        this.body = body;
    }

    public byte[] toBytes() {
        String responseHeader = httpVersion + " " + statusCode.getValue() + " " + statusCode.getPhrase() + CRLF +
                headers.toText() + CRLF;
        byte[] headerBytes = responseHeader.getBytes();
        if (body == null) {
            return headerBytes;
        }
        byte[] responseBytes = new byte[headerBytes.length + body.length];
        System.arraycopy(headerBytes, 0, responseBytes, 0, headerBytes.length);
        System.arraycopy(body, 0, responseBytes, headerBytes.length, body.length);
        return responseBytes;
    }

}
