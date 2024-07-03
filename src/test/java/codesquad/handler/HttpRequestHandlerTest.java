package codesquad.handler;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.http.HttpHeaders;
import codesquad.http.HttpMethod;
import codesquad.http.HttpRequest;
import codesquad.http.HttpRequestFirstLine;
import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import codesquad.http.HttpVersion;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRequestHandlerTest {

    HttpRequestHandler httpRequestHandler = new HttpRequestHandler();

    @Test
    @DisplayName("[Success] /index.html로 요청을 보내면 200 OK 응답이 발생한다.")
    void requestIndex() throws IOException {
        HttpResponse httpResponse = httpRequestHandler.handle(new HttpRequest(
                new HttpRequestFirstLine(HttpVersion.HTTP1_1, HttpMethod.GET, "/index.html"),
                HttpHeaders.empty()
        ));
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("[Success] /indexx.html로 요청을 보내면 400 응답이 발생한다.")
    void incorrectFileNameIndex() throws IOException {
        HttpResponse httpResponse = httpRequestHandler.handle(new HttpRequest(
                new HttpRequestFirstLine(HttpVersion.HTTP1_1, HttpMethod.GET, "/indexx.html"),
                HttpHeaders.empty()
        ));
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}