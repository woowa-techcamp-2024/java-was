package codesquad.configuration;

import codesquad.servlet.SessionStorage;
import codesquad.servlet.execption.GlobalExceptionHandler;
import codesquad.servlet.filter.SessionAuthFilter;
import codesquad.servlet.handler.HandlerMapper;
import codesquad.servlet.handler.HttpRequestHandler;
import codesquad.servlet.handler.resource.MappingMediaTypeFileExtensionResolver;
import codesquad.servlet.handler.resource.StaticResourceHandler;
import codesquad.servlet.handler.resource.StaticResourceReader;
import codesquad.utils.time.CurrentZonedDateTimeGenerator;
import codesquad.utils.time.ZonedDateTimeGenerator;
import codesquad.webserver.Connector;
import codesquad.webserver.HttpProcessor;
import codesquad.webserver.parser.HttpRequestMapper;
import codesquad.webserver.parser.HttpRequestParser;

public class Container {

    public Connector connector() {
        return new Connector(httpProcessor());
    }

    private HttpProcessor httpProcessor() {
        return new HttpProcessor(
                httpRequestMapper(),
                httpRequestHandler(),
                sessionAuthFilter(),
                zonedDateTimeGenerator());
    }

    private GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler(staticResourceReader());
    }

    private HttpRequestMapper httpRequestMapper() {
        return new HttpRequestMapper(httpRequestParser(), globalExceptionHandler());
    }

    private HttpRequestParser httpRequestParser() {
        return new HttpRequestParser();
    }

    private HttpRequestHandler httpRequestHandler() {
        return new HttpRequestHandler(handlerMapper(), staticResourceHandler(), globalExceptionHandler());
    }

    private SessionAuthFilter sessionAuthFilter() {
        return new SessionAuthFilter(sessionStorage());
    }

    private SessionStorage sessionStorage() {
        return SessionStorage.getInstance();
    }

    private HandlerMapper handlerMapper() {
        return HandlerMapper.getInstance();
    }

    private StaticResourceHandler staticResourceHandler() {
        return new StaticResourceHandler(staticResourceReader(), mappingMediaTypeFileExtensionResolver());
    }

    private StaticResourceReader staticResourceReader() {
        return StaticResourceReader.getInstance();
    }

    private MappingMediaTypeFileExtensionResolver mappingMediaTypeFileExtensionResolver() {
        return new MappingMediaTypeFileExtensionResolver();
    }

    private ZonedDateTimeGenerator zonedDateTimeGenerator() {
        return new CurrentZonedDateTimeGenerator();
    }

}
