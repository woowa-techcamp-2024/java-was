package http;

import static util.InputStreamUtil.readLineFromInputStream;

import http.startline.RequestLine;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import util.LoggerUtil;
import util.QueryParserUtil;

public class HttpRequest extends Http {

    private static final Logger logger = LoggerUtil.getLogger();

    public HttpRequest(RequestLine startLine, Header header, byte[] body) {
        super(startLine, header, body);
    }

    public static HttpRequest generateHttpRequest(InputStream inputStream)
        throws IOException {

        final String firstLine = readLineFromInputStream(inputStream);
        if (firstLine == null) {
            throw new IllegalArgumentException("HTTP 메시지가 비어 있습니다.");
        }

        RequestLine startLine = RequestLine.fromString(firstLine);
        Header header = Header.from(inputStream);
        if (header.getValue("Content-Type").startsWith("multipart/form-data")) {
            return new HttpMultiPartRequest(startLine, header, generateBody(inputStream, header));
        }

        byte[] body = generateBody(inputStream, header);
        logger.info("RequestLine: {}", startLine);
        logger.info("Header: {}", header);
        logger.info("Body: {}", new String(body));
        return new HttpRequest(startLine, header, body);
    }

    private static byte[] generateBody(InputStream inputStream, Header header)
        throws IOException {
        if (header.getValue("Content-Length").isEmpty()) {
            return new byte[0];
        }

        int contentLength = Integer.parseInt(header.getValue("Content-Length"));
        byte[] body = new byte[contentLength];
        int nowRead = 0;
        //int temp = inputStream.read(body, 0, contentLength);

        do{
            nowRead += inputStream.read(body, nowRead, contentLength - nowRead);
            if (nowRead == -1) {
                break;
            }
        }while (nowRead < contentLength);

//        for(int i = 0; i < contentLength; i++) {
//            body[i] = (byte) inputStream.read(body, );
//        }

        return body;
    }

    public Map<String, Object> getBodyParams() {
        //this.getbody

        // QueryParserUtil.parseQuery 메소드를 호출하여 쿼리 문자열을 파싱
        Map<String, String> temp = QueryParserUtil.parseQuery(new String(this.getBody()));

        // Map<String, String>을 Map<String, Object>로 변환

        return new HashMap<>(temp);
    }

    public Map<String, String> bodyParamsString() {
        return QueryParserUtil.parseQuery(new String(this.getBody()));
    }
}
