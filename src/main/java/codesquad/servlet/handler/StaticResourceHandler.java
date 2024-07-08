package codesquad.servlet.handler;

import static codesquad.webserver.http.HttpStatus.NOT_FOUND;
import static codesquad.webserver.http.HttpStatus.OK;

import codesquad.servlet.MappingMediaTypeFileExtensionResolver;
import codesquad.servlet.StaticResourceReader;
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
            response.setContentLength(body.length);
            response.setBody(body);

        } catch (FileNotFoundException e) {
            response.setHttpResponseLine(new HttpResponseLine(httpVersion, NOT_FOUND));
            byte[] notFound = NOT_FOUND.getRepresentation()
                    .getBytes(); // TODO: BODY X, 중복된 코드 -> HttpResponse 에서 한 번에 처리하면 되지 않을까?
            response.setBody(notFound);
            response.setContentLength(notFound.length);
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
