package codesquad.webserver.dispatcher;

import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.httpresponse.HttpResponseBuilder;
import codesquad.webserver.httpresponse.HttpResponseWriter;
import codesquad.webserver.requesthandler.RequestHandler;
import codesquad.webserver.router.Router;
import java.io.IOException;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestDispatcher.class);
    private final Router router;
    private final HttpResponseWriter responseWriter;

    public HttpRequestDispatcher(HttpResponseWriter responseWriter) {
        this.router = Router.getInstance();
        this.responseWriter = responseWriter;
    }

    public void dispatch(HttpRequest request, OutputStream outputStream) {
        try {
            RequestHandler handler = router.getHandler(request.requestLine().path());
            HttpResponse response = handler.handle(request);
            responseWriter.writeResponse(outputStream, response);
        } catch (Exception e) {
            handleError(outputStream);
            logger.error(e.getMessage(), e);
        }
    }

    private void handleError(OutputStream outputStream) {
        HttpResponse response = HttpResponseBuilder.buildServerErrorResponse();
        try {
            responseWriter.writeResponse(outputStream, response);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
