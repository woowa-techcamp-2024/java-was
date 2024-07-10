package http;

import exception.GeneralException;
import http.startline.ResponseLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseValueSetter {

    public static final String CRLF = "\r\n";
    public static final String HTTP_VERSION = "HTTP/1.1";
    private static final Logger logger = LoggerFactory.getLogger(ResponseValueSetter.class);

    public static void success(HttpResponse httpResponse, byte[] body) {
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        Header responseHeader = httpResponse.getHeader();

        // start line
        responseLine.setVersion(HTTP_VERSION);
        responseLine.setStatusCode(200);
        responseLine.setStatusMessage("OK");

        // header
        responseHeader.addKey("Content-Length", String.valueOf(body.length));

        // body
        httpResponse.setBody(body);
    }

    public static void success(HttpResponse httpResponse) {
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        Header responseHeader = httpResponse.getHeader();

        // start line
        responseLineSet(responseLine, HTTP_VERSION, 200, "OK");

        // header
        responseHeader.addKey("Content-Length", "0");

        // body
        httpResponse.setBody(new byte[0]);
    }

    public static void redirect(HttpRequest httpRequest, HttpResponse httpResponse,
        String urlPath) {
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();

        responseLineSet(responseLine, HTTP_VERSION, 302, "Found");

        Header responseHeader = httpResponse.getHeader();
        responseHeader.addKey("Location", urlPath);

        httpResponse.setStartLine(responseLine);
    }

    public static void fail(HttpResponse httpResponse, GeneralException error) {
        logger.error("HTTP REQUEST ERROR: {}", error.getMessage());
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        Header responseHeader = httpResponse.getHeader();
        // error의 코드를 받아온다.
        responseLineSet(responseLine, HTTP_VERSION, error.getStatusCode(), error.getMessage());

        responseHeader.addKey("Content-Length", "0");

        httpResponse.setBody(new byte[0]);
    }

    private static void responseLineSet(
        ResponseLine responseLine,
        String httpVersion,
        int statusCode,
        String statusMessage
    ) {
        responseLine.setVersion(HTTP_VERSION);
        responseLine.setStatusCode(statusCode);
        responseLine.setStatusMessage(statusMessage);
    }
}
