package codesquad.handler;

import codesquad.http.HttpHeaders;
import codesquad.http.HttpMediaType;
import codesquad.http.HttpMethod;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import codesquad.http.HttpVersion;
import java.io.IOException;

public class HttpRequestHandler {

    public HttpResponse handle(final HttpRequest httpRequest) throws IOException {
        // TODO: reuqetURL 이 API인지, 정적 리소스 요청인지 구분하는 로직 필요
        HttpVersion httpVersion = httpRequest.getVersion();
        HttpMethod method = httpRequest.getMethod();
        String path = httpRequest.getPath();

        if (method == HttpMethod.GET && path.equals("/index.html")) {
            String content = StaticResourceHandler.getFileContents(StaticResourceHandler.STATIC_PATH + path);
            HttpHeaders httpHeaders = HttpHeaders.empty();
            httpHeaders.setContentType(HttpMediaType.TEXT_HTML);
            return new HttpResponse(httpVersion, HttpStatus.OK, httpHeaders, content);
        }

        return new HttpResponse(httpVersion, HttpStatus.BAD_REQUEST, HttpHeaders.empty(), "");
    }

}
