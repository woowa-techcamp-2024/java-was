package codesquad.servlet.execption;

import static codesquad.webserver.http.HttpStatus.BAD_REQUEST;
import static codesquad.webserver.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static codesquad.webserver.http.HttpStatus.METHOD_NOT_ALLOWED;
import static codesquad.webserver.http.HttpStatus.NOT_FOUND;

import codesquad.servlet.handler.resource.StaticResourceReader;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final StaticResourceReader staticResourceReader;

    public GlobalExceptionHandler(StaticResourceReader staticResourceReader) {
        this.staticResourceReader = staticResourceReader;
    }

    public void handle(ClientException clientException, HttpResponse httpResponse) {
        logger.error(clientException.getMessage(), clientException);

        if (clientException.getErrorCode() == ErrorCode.INVALID_USER_LOGIN) {
            httpResponse.sendRedirect("/user/fail");
            return;
        }

        if (clientException.getErrorCode() == ErrorCode.ALREADY_REGISTERED_USER_ID
                || clientException.getErrorCode() == ErrorCode.ALREADY_REGISTERED_EMAIL) {
            httpResponse.sendRedirect("/registration/fail");
            return;
        }

        HttpStatus httpStatus = clientException.getHttpStatus();
        if (httpStatus == NOT_FOUND) {
            byte[] fileContents = staticResourceReader.getFileContents("/error/404.html");
            httpResponse.badResponse(NOT_FOUND, fileContents);
            return;
        }

        if (httpStatus == BAD_REQUEST) {
            byte[] fileContents = staticResourceReader.getFileContents("/error/400.html");
            httpResponse.badResponse(BAD_REQUEST, fileContents);
            return;
        }

        if (httpStatus == METHOD_NOT_ALLOWED) {
            byte[] fileContents = staticResourceReader.getFileContents("/error/405.html");
            httpResponse.badResponse(METHOD_NOT_ALLOWED, fileContents);
        }
    }

    public void handle(ServerException serverException, HttpResponse httpResponse) {
        logger.error(serverException.getMessage(), serverException);
        byte[] fileContents = staticResourceReader.getFileContents("/error/500.html");
        httpResponse.badResponse(INTERNAL_SERVER_ERROR, fileContents);
    }

}
