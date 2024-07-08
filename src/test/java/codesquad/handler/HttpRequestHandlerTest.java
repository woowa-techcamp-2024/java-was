package codesquad.handler;

import static codesquad.webserver.http.HttpMethod.GET;
import static codesquad.webserver.http.HttpVersion.HTTP1_1;
import static org.assertj.core.api.Assertions.assertThat;

import codesquad.servlet.HandlerMapper;
import codesquad.servlet.MappingMediaTypeFileExtensionResolver;
import codesquad.servlet.StaticResourceReader;
import codesquad.servlet.handler.HttpRequestHandler;
import codesquad.servlet.handler.StaticResourceHandler;
import codesquad.utils.FixedZonedDateTimeGenerator;
import codesquad.webserver.http.HttpHeaders;
import codesquad.webserver.http.HttpPath;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpRequestLine;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.http.HttpStatus;
import java.io.IOException;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRequestHandlerTest {

    FixedZonedDateTimeGenerator fixedZonedDateTimeGenerator = new FixedZonedDateTimeGenerator(2000, 1, 1, 1, 1, 1, 1,
            ZoneOffset.UTC);

    HttpRequestHandler httpRequestHandler = new HttpRequestHandler(
            new HandlerMapper(),
            new StaticResourceHandler(
                    new StaticResourceReader(),
                    new MappingMediaTypeFileExtensionResolver()
            ),
            fixedZonedDateTimeGenerator
    );

    @Test
    @DisplayName("[Success] /index.html로 요청을 보내면 200 OK 응답이 발생한다.")
    void requestIndex() throws IOException {
        HttpResponse httpResponse = HttpResponse.ok();
        httpRequestHandler.handle(new HttpRequest(
                new HttpRequestLine(GET, HttpPath.ofOnlyDefaultPath("/index.html"), HTTP1_1),
                HttpHeaders.empty()), new HttpResponse(null, HttpHeaders.empty(), null)
        );
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("[Success] /indexx.html로 요청을 보내면 400 응답이 발생한다.")
    void incorrectFileNameIndex() throws IOException {
        HttpResponse httpResponse = HttpResponse.ok();
        httpRequestHandler.handle(new HttpRequest(
                new HttpRequestLine(GET, HttpPath.ofOnlyDefaultPath("/indexx.html"), HTTP1_1),
                HttpHeaders.empty()), httpResponse
        );
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}