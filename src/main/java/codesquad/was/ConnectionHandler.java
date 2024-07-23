package codesquad.was;

import static codesquad.was.http.type.StatusCodeType.NOT_FOUND;

import codesquad.was.exception.InternalServerException;
import codesquad.was.http.HttpRequest;
import codesquad.was.http.HttpResponse;
import codesquad.was.http.type.MimeType;
import codesquad.web.snippet.ResourceSnippetBuilder;
import codesquad.web.snippet.Snippet;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionHandler implements Runnable {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Socket clientSocket;
    private final RequestHandlerMapping requestHandlerMapping;

    public ConnectionHandler(final Socket clientSocket, final RequestHandlerMapping requestHandlerMapping) {
        this.clientSocket = clientSocket;
        this.requestHandlerMapping = requestHandlerMapping;
    }

    @Override
    public void run() {
        try (InputStream clientInput = clientSocket.getInputStream();
             OutputStream clientOutput = clientSocket.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(clientInput);
            log.debug("Http Request = {}", httpRequest);

            HttpResponse httpResponse = new HttpResponse(clientOutput, httpRequest.getHttpVersion());

            RequestHandler requestHandler = requestHandlerMapping.read(httpRequest.getRequestPath());
            validateHandler(httpRequest, httpResponse, requestHandler);

            requestHandler.process(httpRequest, httpResponse);
        } catch (IOException e) {
            log.error("요청을 처리할 수 없습니다.", e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new InternalServerException();
            }
        }
    }

    private void validateHandler(final HttpRequest httpRequest,
                                 final HttpResponse httpResponse,
                                 final RequestHandler requestHandler)
            throws IOException {
        if (Objects.isNull(requestHandler)) {
            log.error("요청 핸들러를 찾을 수 없습니다. requestPath = {}", httpRequest.getRequestPath());
            responseNotFound(httpRequest, httpResponse);
        }
    }

    private void responseNotFound(final HttpRequest httpRequest, final HttpResponse httpResponse) throws IOException {
        String errorPage = ResourceSnippetBuilder.builder()
                .templatePath("/error/error-template.html")
                .snippets(List.of(new Snippet(NOT_FOUND.getStatusCode()),
                        new Snippet("요청을 찾을 수 없습니다. requestPath = " + httpRequest.getRequestPath())))
                .build()
                .getCompleteSnippet();

        httpResponse.sendError(errorPage.getBytes(), NOT_FOUND, MimeType.HTML);
    }
}
