package codesquad.handler;

import codesquad.error.HttpStatusException;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.MediaType;
import codesquad.http.StatusCode;
import codesquad.resource.DirectoryIndexResolver;
import codesquad.resource.Resource;

import java.util.Optional;

public final class StaticResourceHandler extends RequestHandler {

    private static StaticResourceHandler instance;

    private final DirectoryIndexResolver directoryIndexResolver = DirectoryIndexResolver.getInstance();

    private StaticResourceHandler() {
    }

    public static StaticResourceHandler getInstance() {
        if (instance == null) {
            instance = new StaticResourceHandler();
        }
        return instance;
    }

    @Override
    protected HttpResponse handleGet(HttpRequest httpRequest) {
        String uri = httpRequest.uri();

        Optional<Resource> resolvedResource = directoryIndexResolver.resolve(uri);
        if (resolvedResource.isPresent()) {
            Resource resource = resolvedResource.get();
            MediaType mediaType = MediaType.find(resource.getExtension());
            return responseGenerator.sendOK(resource.getContent(), mediaType, httpRequest);
        }
        throw new HttpStatusException(StatusCode.NOT_FOUND, "[ERROR] 파일을 찾을 수 없습니다.");
    }
}
