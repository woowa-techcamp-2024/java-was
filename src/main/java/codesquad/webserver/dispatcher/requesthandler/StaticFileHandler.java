package codesquad.webserver.dispatcher.requesthandler;

import static codesquad.webserver.httpresponse.HttpResponseBuilder.build;
import static codesquad.webserver.httpresponse.HttpResponseBuilder.buildNotFoundResponse;

import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import java.io.IOException;

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
