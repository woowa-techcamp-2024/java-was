package codesquad.handler;

import codesquad.file.FileReader;
import codesquad.http.HttpStatus;
import codesquad.http.request.HttpRequest;
import codesquad.http.response.HttpResponse;
import codesquad.util.DirectoryMapper;

public class StaticResourceHandler implements Handler {

    @Override
    public HttpResponse handle(HttpRequest request) throws RuntimeException {
        if(request.method().equals("GET")) {
            return doGet(request);
        }
        return new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED, "text", new byte[0]);
    }

    public HttpResponse doGet(HttpRequest request) throws RuntimeException {
        String mappedPath = DirectoryMapper.getStaticResourcePath(request.path());
        if (mappedPath != null) {
            return new HttpResponse(HttpStatus.OK, mappedPath, FileReader.getContent(mappedPath));
        }
        return new HttpResponse(HttpStatus.OK, request.path(), FileReader.getContent(request.path()));
    }
}
