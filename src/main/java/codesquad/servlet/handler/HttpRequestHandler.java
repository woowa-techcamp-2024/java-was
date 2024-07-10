package codesquad.servlet.handler;

import codesquad.servlet.handler.resource.StaticResourceHandler;
import codesquad.utils.time.ZonedDateTimeGenerator;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpPath;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);

    private final HandlerMapper handlerMapper;
    private final StaticResourceHandler staticResourceHandler;
    private final ZonedDateTimeGenerator zonedDateTimeGenerator;

    public HttpRequestHandler(HandlerMapper handlerMapper,
                              StaticResourceHandler staticResourceHandler,
                              ZonedDateTimeGenerator zonedDateTimeGenerator
    ) {
        this.handlerMapper = handlerMapper;
        this.staticResourceHandler = staticResourceHandler;
        this.zonedDateTimeGenerator = zonedDateTimeGenerator;
    }

    public void handle(final HttpRequest httpRequest, final HttpResponse httpResponse) {
        HttpMethod method = httpRequest.getMethod();
        HttpPath path = httpRequest.getPath();

        Optional<Handler> handler = handlerMapper.findBy(method, path.getDefaultPath());
        if (handler.isPresent()) {
            Handler userRegistrationHandler = handler.get();
            userRegistrationHandler.service(httpRequest, httpResponse);
            httpResponse.setDefaultHeaders(zonedDateTimeGenerator.now());
            return;
        }

        if (isStaticResourceRequest(method, path)) {
            staticResourceHandler.service(httpRequest, httpResponse);
            httpResponse.setDefaultHeaders(zonedDateTimeGenerator.now());
        }
    }

    private boolean isStaticResourceRequest(HttpMethod method, HttpPath path) {
        if (method == HttpMethod.GET) {
            return path.hasFileExtension() || path.isDirectoryPath();
        }
        return false;
    }


}
