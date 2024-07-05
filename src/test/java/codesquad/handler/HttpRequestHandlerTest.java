package codesquad.handler;

import static codesquad.http.HttpMethod.GET;
import static codesquad.http.HttpVersion.HTTP1_1;
import static org.assertj.core.api.Assertions.assertThat;

import codesquad.http.HttpHeaders;
import codesquad.http.HttpRequest;
import codesquad.http.HttpRequestFirstLine;
import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRequestHandlerTest {

    HttpRequestHandler httpRequestHandler = new HttpRequestHandler(new StaticResourceHandler(),
            new MappingMediaTypeFileExtensionResolver());

    @Test
    @DisplayName("[Success] /index.html로 요청을 보내면 200 OK 응답이 발생한다.")
    void requestIndex() throws IOException {
        HttpResponse httpResponse = httpRequestHandler.handle(new HttpRequest(
                new HttpRequestFirstLine(GET, "/index.html", HTTP1_1),
                HttpHeaders.empty()
        ));
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("[Success] /indexx.html로 요청을 보내면 400 응답이 발생한다.")
    void incorrectFileNameIndex() throws IOException {
        HttpResponse httpResponse = httpRequestHandler.handle(new HttpRequest(
                new HttpRequestFirstLine(GET, "/indexx.html", HTTP1_1),
                HttpHeaders.empty()
        ));
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}