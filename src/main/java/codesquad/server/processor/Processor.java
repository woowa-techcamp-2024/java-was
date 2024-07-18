package codesquad.server.processor;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.constant.HttpStatus;
import codesquad.server.processor.message.HttpGenerator;
import codesquad.server.processor.message.HttpParser;
import codesquad.security.SessionFilter;

import java.io.IOException;

public class Processor {
    public HttpRequest processRequest(byte[] inputMessage) throws Exception {
        HttpRequest httpRequest = HttpParser.parseHttpRequest(inputMessage);
        SessionFilter.filter(httpRequest);
        return httpRequest;
    }

    public byte[] processResponse(HttpResponse httpResponse) throws IOException {
        HttpStatus httpStatus = httpResponse.getResponseStartLine().status();
        if (httpStatus != HttpStatus.OK && httpStatus != HttpStatus.FOUND) return HttpGenerator.generateException(httpResponse);
        return HttpGenerator.generate(httpResponse);
    }
}
