package http;

import http.startline.RequestLine;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import util.QueryParserUtil;

public class HttpRequest extends Http {

    public HttpRequest(RequestLine startLine, Header header, byte[] body) {
        super(startLine, header, body);
    }

    public static HttpRequest generateHttpRequest(BufferedReader bufferedReader)
        throws IOException {
        final String firstLine = bufferedReader.readLine();
        if (firstLine == null) {
            throw new IllegalArgumentException("HTTP 메시지가 비어 있습니다.");
        }

        RequestLine startLine = RequestLine.fromString(firstLine);
        Header header = Header.from(bufferedReader);
        byte[] body = generateBody(bufferedReader, header);
        return new HttpRequest(startLine, header, body);
    }

    private static byte[] generateBody(BufferedReader bufferedReader, Header header)
        throws IOException {
        if (header.getValue("Content-Length").isEmpty()) {
            return new byte[0];
        }

        int contentLength = Integer.parseInt(header.getValue("Content-Length"));
        char[] body = new char[contentLength];
        bufferedReader.read(body, 0, contentLength);

        return new String(body).getBytes();
    }

    public Map<String, String> getBodyParams() {
        return QueryParserUtil.parseQuery(new String(this.getBody()));
    }
}
