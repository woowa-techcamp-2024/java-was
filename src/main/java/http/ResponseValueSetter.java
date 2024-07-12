package http;

import static util.HeaderStringUtil.CONTENT_LENGTH;
import static util.HeaderStringUtil.LOCATION;
import static util.HttpStatusCode.FOUND;
import static util.HttpStatusCode.OK;

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
        responseLineSet(responseLine, HTTP_VERSION, OK.getStatusCode(), OK.getStatusMessage());

        // header
        responseHeader.addKey(CONTENT_LENGTH, String.valueOf(body.length));

        // body
        httpResponse.setBody(body);
    }

    public static void success(HttpResponse httpResponse) {
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        Header responseHeader = httpResponse.getHeader();

        // start line
        responseLineSet(responseLine, HTTP_VERSION, OK.getStatusCode(), OK.getStatusMessage());

        // header
        responseHeader.addKey(CONTENT_LENGTH, "0");

        // body
        httpResponse.setBody(new byte[0]);
    }

    public static void redirect(HttpRequest httpRequest, HttpResponse httpResponse,
        String urlPath) {
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();

        responseLineSet(responseLine, HTTP_VERSION, FOUND.getStatusCode(), FOUND.getStatusMessage());

        Header responseHeader = httpResponse.getHeader();
        responseHeader.addKey(LOCATION, urlPath);

        httpResponse.setStartLine(responseLine);
    }

    public static void fail(HttpResponse httpResponse, GeneralException error) {
        logger.error("HTTP REQUEST ERROR: {}", error.getMessage());
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        Header responseHeader = httpResponse.getHeader();
        // error의 코드를 받아온다.
        responseLineSet(responseLine, HTTP_VERSION, error.getStatusCode(), error.getMessage());

        responseHeader.addKey(CONTENT_LENGTH, "0");

        httpResponse.setBody(new byte[0]);
    }

    private static void responseLineSet(
        ResponseLine responseLine,
        String httpVersion,
        int statusCode,
        String statusMessage
    ) {
        responseLine.setVersion(httpVersion);
        responseLine.setStatusCode(statusCode);
        responseLine.setStatusMessage(statusMessage);
    }
}
