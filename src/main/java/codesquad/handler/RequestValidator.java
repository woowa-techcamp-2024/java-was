package codesquad.handler;

import codesquad.error.HttpRequestException;
import codesquad.http.HttpHeaders;
import codesquad.http.HttpRequest;
import codesquad.http.MediaType;
import codesquad.http.StatusCode;

public class RequestValidator {

    public static void validateContentType(HttpRequest httpRequest) {
        String contentType = httpRequest.firstHeaderValue(HttpHeaders.CONTENT_TYPE)
                .orElse("");
        if (!contentType.equals(MediaType.APPLICATION_FORM_URLENCODED.getValue())) {
            throw new HttpRequestException(StatusCode.BAD_REQUEST, "[ERROR] request body 타입이 올바르지 않습니다.");
        }
    }
}
