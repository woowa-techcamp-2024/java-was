package codesquad.servlet.handler.resource;

import static codesquad.webserver.http.HttpStatus.OK;

import codesquad.servlet.handler.Handler;
import codesquad.webserver.http.HttpMediaType;
import codesquad.webserver.http.HttpPath;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.http.HttpResponseLine;
import codesquad.webserver.http.HttpVersion;
import java.io.FileNotFoundException;
import java.io.IOException;

public class StaticResourceHandler implements Handler {

    private final StaticResourceReader staticResourceReader;
    private final MappingMediaTypeFileExtensionResolver mappingMediaTypeFileExtensionResolver;

    public StaticResourceHandler(StaticResourceReader staticResourceReader,
                                 MappingMediaTypeFileExtensionResolver mappingMediaTypeFileExtensionResolver
    ) {
        this.staticResourceReader = staticResourceReader;
        this.mappingMediaTypeFileExtensionResolver = mappingMediaTypeFileExtensionResolver;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpVersion httpVersion = request.getVersion();
        HttpPath path = request.getPath();
        try {
            String pathValue = path.getDefaultPath();
            if (path.isDirectoryPath()) {
                if (pathValue.charAt(pathValue.length() - 1) == '/') {
                    pathValue += "index.html";
                } else {
                    pathValue += "/index.html";
                }
            }
            String fileExtension = getFileExtension(pathValue);
            HttpMediaType httpMediaType = mappingMediaTypeFileExtensionResolver.resolve(fileExtension);
            byte[] body = staticResourceReader.getFileContents(pathValue);

            response.setHttpResponseLine(new HttpResponseLine(httpVersion, OK));
            response.setContentType(httpMediaType);
            response.setBody(body);

        } catch (FileNotFoundException e) {
            response.notFound();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFileExtension(String path) {
        int lastIndexOfDot = path.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return "";
        }
        return path.substring(lastIndexOfDot + 1);
    }

}
