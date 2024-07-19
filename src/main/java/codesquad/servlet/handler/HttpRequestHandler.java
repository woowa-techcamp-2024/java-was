package codesquad.servlet.handler;

import static codesquad.webserver.http.HttpStatus.INTERNAL_SERVER_ERROR;

import codesquad.servlet.execption.ClientException;
import codesquad.servlet.execption.GlobalExceptionHandler;
import codesquad.servlet.execption.ServerException;
import codesquad.servlet.handler.resource.StaticResourceHandler;
import codesquad.webserver.http.Cookie;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpPath;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.http.HttpStatus;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);

    private final HandlerMapper handlerMapper;
    private final StaticResourceHandler staticResourceHandler;
    private final GlobalExceptionHandler globalExceptionHandler;

    private List<String> staticAuthPathes = List.of("/user/list");

    public HttpRequestHandler(HandlerMapper handlerMapper, StaticResourceHandler staticResourceHandler,
                              GlobalExceptionHandler globalExceptionHandler) {
        this.handlerMapper = handlerMapper;
        this.staticResourceHandler = staticResourceHandler;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    public void handle(final HttpRequest httpRequest, final HttpResponse httpResponse) {
        HttpMethod method = httpRequest.getMethod();
        HttpPath path = httpRequest.getPath();
        Optional<Handler> handler = handlerMapper.findBy(method, path.getDefaultPath());
        try {
            if (handler.isPresent()) {
                Handler userRegistrationHandler = handler.get();
                userRegistrationHandler.service(httpRequest, httpResponse);
                return;
            }
            if (isStaticResourceRequest(method, path)) {
                if (validateStaticAuthPath(httpRequest, httpResponse, path)) {
                    return;
                }
                staticResourceHandler.service(httpRequest, httpResponse);
                return;
            }

            throw new ClientException("잘못된 요청입니다", HttpStatus.BAD_REQUEST);

        } catch (ClientException e) {
            globalExceptionHandler.handle(e, httpResponse);

        } catch (ServerException e) {
            globalExceptionHandler.handle(e, httpResponse);

        } catch (Exception e) {
            globalExceptionHandler.handle(new ServerException(e, INTERNAL_SERVER_ERROR), httpResponse);
        }
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
