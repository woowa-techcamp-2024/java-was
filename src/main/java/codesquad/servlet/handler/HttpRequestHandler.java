package codesquad.servlet.handler;

import codesquad.servlet.handler.resource.StaticResourceHandler;
import codesquad.utils.time.ZonedDateTimeGenerator;
import codesquad.webserver.http.Cookie;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpPath;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);

    private final HandlerMapper handlerMapper;
    private final StaticResourceHandler staticResourceHandler;
    private final ZonedDateTimeGenerator zonedDateTimeGenerator;

    private List<String> staticAuthPathes = List.of("/user/list");

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
        try {
            if (handler.isPresent()) {
                Handler userRegistrationHandler = handler.get();
                userRegistrationHandler.service(httpRequest, httpResponse);
                httpResponse.setDefaultHeaders(zonedDateTimeGenerator.now());
                return;
            }
        } catch (IllegalArgumentException e) {
            httpResponse.setDefaultHeaders(zonedDateTimeGenerator.now());
            httpResponse.sendRedirect("/user/fail");
            logger.debug("error while handling request", e);
            return;
        }

        if (isStaticResourceRequest(method, path)) {
            if (validateStaticAuthPath(httpRequest, httpResponse, path)) {
                return;
            }
            staticResourceHandler.service(httpRequest, httpResponse);
            httpResponse.setDefaultHeaders(zonedDateTimeGenerator.now());
            return;
        }

        httpResponse.setDefaultHeaders(zonedDateTimeGenerator.now());
        httpResponse.notFound();
    }

    private boolean validateStaticAuthPath(HttpRequest httpRequest, HttpResponse httpResponse, HttpPath path) {
        if (staticAuthPathes.contains(path.getDefaultPath())) {
            Cookie cookie = httpRequest.getCookie();
            logger.debug("cookie: {}", cookie);
            if (cookie == null || !cookie.getName().equals("sid")) {
                httpResponse.sendRedirect("/login");
                return true;
            }
        }
        return false;
    }

    private boolean isStaticResourceRequest(HttpMethod method, HttpPath path) {
        if (method == HttpMethod.GET) {
            return path.hasFileExtension() || path.isDirectoryPath();
        }
        return false;
    }

}
