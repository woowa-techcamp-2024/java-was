package codesquad.webserver.parser;

import codesquad.webserver.httprequest.HttpRequest;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BodyParser {

    private static final Logger logger = LoggerFactory.getLogger(BodyParser.class);
    private static final String UTF_8 = "UTF-8";

    public static void parse(BufferedInputStream in, HttpRequest request) throws IOException {
        List<String> contentTypeHeaders = request.getHeaders().get("Content-Type");
        if (contentTypeHeaders != null && !contentTypeHeaders.isEmpty()) {
            String contentType = contentTypeHeaders.get(0);
            if (contentType.startsWith("multipart/form-data")) {
                MultipartParser.parse(in, request);
            } else if (contentType.equals("application/x-www-form-urlencoded")) {
                parseFormUrlEncoded(in, request);
            } else {
                parseRegularBody(in, request);
            }
        }
    }

    private static void parseRegularBody(BufferedInputStream in, HttpRequest request) throws IOException {
        List<String> contentLengthHeaders = request.getHeaders().get("Content-Length");
        if (contentLengthHeaders != null && !contentLengthHeaders.isEmpty()) {
            int contentLength = Integer.parseInt(contentLengthHeaders.get(0));
            byte[] bodyBytes = new byte[contentLength];
            int bytesRead = in.read(bodyBytes);
            if (bytesRead != contentLength) {
                throw new IOException("Unexpected end of body");
            }
            String body = new String(bodyBytes, UTF_8);
            request.setBody(body);
            return;
        }
        request.setBody("");
    }

    private static void parseFormUrlEncoded(BufferedInputStream in, HttpRequest request) throws IOException {
        String body = parseRegularBodyAsString(in, request);
        Map<String, String> params = QueryStringParser.parse(body);
        request.setParams(params);
        logger.debug("Parsed form data parameters: {}", params);
    }

    private static String parseRegularBodyAsString(BufferedInputStream in, HttpRequest request) throws IOException {
        List<String> contentLengthHeaders = request.getHeaders().get("Content-Length");
        if (contentLengthHeaders != null && !contentLengthHeaders.isEmpty()) {
            int contentLength = Integer.parseInt(contentLengthHeaders.get(0));
            byte[] bodyBytes = new byte[contentLength];
            int bytesRead = in.read(bodyBytes);
            if (bytesRead != contentLength) {
                throw new IOException("Unexpected end of body");
            }
            return new String(bodyBytes, UTF_8);
        }
        return "";
    }
}