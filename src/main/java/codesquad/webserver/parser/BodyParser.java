package codesquad.webserver.parser;

import codesquad.webserver.httprequest.HttpRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BodyParser {

    private static final Logger logger = LoggerFactory.getLogger(BodyParser.class);

    public static void parse(BufferedReader in, HttpRequest request) throws IOException {
        List<String> contentTypes = request.getHeaders().get("Content-Type");
        if (contentTypes != null && !contentTypes.isEmpty() && contentTypes.get(0).startsWith("multipart/form-data")) {
             MultipartParser.parse(in, request);
        } else {
            StringBuilder body = new StringBuilder();
            List<String> contentLengths = request.getHeaders().get("Content-Length");
            if (contentLengths != null && !contentLengths.isEmpty()) {
                try {
                    int contentLength = Integer.parseInt(contentLengths.get(0));
                    char[] buffer = new char[contentLength];
                    int read = in.read(buffer, 0, contentLength);
                    body.append(buffer, 0, read);
                    logger.debug("Body : {}", body);
                } catch (NumberFormatException e) {
                    logger.error("Invalid Content-Length value: {}", contentLengths.get(0));
                    throw new IOException("Invalid Content-Length value: " + contentLengths.get(0), e);
                }
            }
            request.setBody(body.toString());
        }
    }
}