package codesquad.webserver.requesthandler;

import static codesquad.webserver.httpresponse.HttpResponseBuilder.build;
import static codesquad.webserver.httpresponse.HttpResponseBuilder.buildNotFoundResponse;

import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import java.io.IOException;

public class RegisterRequestHandler extends AbstractRequestHandler {

    private static final String FILE_PATH = "/registration";

    public RegisterRequestHandler(FileReader fileReader) {
        super(fileReader);
    }

    @Override
    protected HttpResponse handleGet(HttpRequest request) {
        try {
            FileReader.FileResource file = fileReader.read(FILE_PATH);
            return build(file);
        } catch (IOException e) {
            return buildNotFoundResponse();
        }
    }
}
