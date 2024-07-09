package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.MediaType;
import codesquad.resource.Resource;
import codesquad.resource.ResourcesReader;

import java.util.Optional;

public final class StaticResourceHandler extends RequestHandler {

    private static StaticResourceHandler INSTANCE = new StaticResourceHandler();

    private StaticResourceHandler() {
    }

    public static StaticResourceHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StaticResourceHandler();
        }
        return INSTANCE;
    }

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        String uri = httpRequest.uri();

        Optional<Resource> readResource = ResourcesReader.readResource(uri);
        if (readResource.isPresent()) {
            Resource resource = readResource.get();
            if (resource.isFile()) {
                MediaType mediaType = MediaType.find(resource.getExtension());
                return responseGenerator.sendOK(resource.getContent(), mediaType, httpRequest);
            }

            readResource = ResourcesReader.readResource(resource.getFileName() + "/index.html");
            if (readResource.isPresent()) {
                resource = readResource.get();
                MediaType mediaType = MediaType.find(resource.getExtension());
                return responseGenerator.sendOK(resource.getContent(), mediaType, httpRequest);
            }
        }
        return responseGenerator.sendNotFound(httpRequest);
    }

}
