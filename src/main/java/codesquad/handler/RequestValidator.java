package codesquad.handler;

import codesquad.http.HttpHeaders;
import codesquad.http.HttpRequest;
import codesquad.http.MediaType;

public class RequestValidator {

    public static void validateContentType(HttpRequest httpRequest) {
        String contentType = httpRequest.firstHeaderValue(HttpHeaders.CONTENT_TYPE)
                .orElse("");
        if (!contentType.equals(MediaType.APPLICATION_FORM_URLENCODED.getValue())) {
            // TODO 400 Bad Request 적용
            throw new IllegalArgumentException("[ERROR] request body 타입이 올바르지 않습니다.");
        }
    }

}
