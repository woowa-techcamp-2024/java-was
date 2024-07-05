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

    private final StaticResourceHandler staticResourceHandler;
    private final MappingMediaTypeFileExtensionResolver mappingMediaTypeFileExtensionResolver;

    public HttpRequestHandler(StaticResourceHandler staticResourceHandler,
                              MappingMediaTypeFileExtensionResolver mappingMediaTypeFileExtensionResolver
    ) {
        this.staticResourceHandler = staticResourceHandler;
        this.mappingMediaTypeFileExtensionResolver = mappingMediaTypeFileExtensionResolver;
    }

    public HttpResponse handle(final HttpRequest httpRequest) throws IOException {
        // TODO: reuqetURL 이 API인지, 정적 리소스 요청인지 구분하는 로직 필요
        HttpVersion httpVersion = httpRequest.getVersion();
        HttpMethod method = httpRequest.getMethod();
        String path = httpRequest.getPath();

        if (method == HttpMethod.GET) {
            String fileExtension = getFileExtension(path);
            HttpMediaType httpMediaType = mappingMediaTypeFileExtensionResolver.resolve(fileExtension);

            byte[] body = staticResourceHandler.getFileContents(path);

            HttpHeaders httpHeaders = HttpHeaders.empty();
            httpHeaders.setContentType(httpMediaType);
            httpHeaders.setContentLength(body.length);

            return new HttpResponse(httpVersion, HttpStatus.OK, httpHeaders, body);
        }

        return new HttpResponse(httpVersion, HttpStatus.BAD_REQUEST, HttpHeaders.empty(), new byte[0]);
    }

    private String getFileExtension(String path) {
        int lastIndexOfDot = path.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return "";
        }
        return path.substring(lastIndexOfDot + 1);
    }

}
