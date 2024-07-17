package codesquad.webserver.staticresouce;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.annotation.Component;
import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.httpresponse.HttpResponseBuilder;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class StaticResourceHandler {

    private static final Logger logger = LoggerFactory.getLogger(StaticResourceHandler.class);
    private final FileReader fileReader;
    private final StaticResourceResolver resourceResolver;

    @Autowired
    public StaticResourceHandler(FileReader fileReader, StaticResourceResolver resourceResolver) {
        this.fileReader = fileReader;
        this.resourceResolver = resourceResolver;
    }

    public HttpResponse handleRequest(HttpRequest request) {
        String path = request.getRequestLine().getPath();
        try {
            FileReader.FileResource resource = fileReader.read(path);
            return HttpResponseBuilder.buildFromFile(resource);
        } catch (IOException e) {
            logger.debug("Error reading file {}", path, e);
            return HttpResponseBuilder.notFound().build();
        }
    }
}
