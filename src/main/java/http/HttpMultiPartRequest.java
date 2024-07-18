package http;

import http.startline.RequestLine;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import util.CookieUtil;
import util.FileUtil;
import util.LoggerUtil;
import util.QueryParserUtil;

public class HttpMultiPartRequest extends HttpRequest{

    private static final Logger logger = LoggerUtil.getLogger();
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT = "content";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";

    // bodyparam의 content-type을 저장하는 Map
    private Map<String, String> contentTypeParams = new HashMap<>();

    public HttpMultiPartRequest(RequestLine startLine, Header header, byte[] body) {
        super(startLine, header, body);
    }

    public Optional<String> getContentTypeParamsValue(String key) {
        return Optional.ofNullable(contentTypeParams.get(key));
    }

    public void setContentTypeParamsValue(String key, String value) {
        contentTypeParams.put(key, value);
    }

    public Map<String, Map<String, byte[]>> multipartBodyParams() {
        // body를 multipart로 나누어 Map<String, Map<String, byte[]>>으로 변환
        // this.getBody();
        String boundary = QueryParserUtil.getBoundaryFromContentType(getHeader().getValue("Content-Type"));
        if (boundary == null) {
            throw new IllegalArgumentException("Boundary not found in Content-Type header");
        }
        return QueryParserUtil.parseMulti(this.getBody(), boundary);
    }

    public String getFileExtension(String parameterName){
        // parameterName에 해당하는 파일의 확장자를 반환
        var data = multipartBodyParams();
        logger.info("data: {}", new String(data.get(parameterName).get(CONTENT_DISPOSITION)));
        String fileName = CookieUtil.getCookieValue(new String(data.get(parameterName).get(CONTENT_DISPOSITION)), "filename");
        // 만약에 앞 뒤로 "가 있다면 제거
        if (fileName.startsWith("\"") && fileName.endsWith("\""))
            fileName = fileName.substring(1, fileName.length() - 1);

        return FileUtil.getFileExtension(fileName);
    }
}
