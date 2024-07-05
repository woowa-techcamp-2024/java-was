package codesquad.webserver.requesthandler;

import static codesquad.webserver.httpresponse.HttpResponseBuilder.build;
import static codesquad.webserver.httpresponse.HttpResponseBuilder.buildNotFoundResponse;

import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class StaticFileHandler extends AbstractRequestHandler {

    public StaticFileHandler(FileReader fileReader) {
        super(fileReader);
    }

    @Override
    protected HttpResponse handleGet(HttpRequest request) {
        try {
            FileReader.FileResource file = fileReader.read(request.requestLine().path());
            return build(file);
        } catch (IOException e) {
            return buildNotFoundResponse();
        }
    }
}
