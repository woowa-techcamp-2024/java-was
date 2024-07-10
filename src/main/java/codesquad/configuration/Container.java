package codesquad.configuration;

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
        return new HttpProcessor(httpRequestMapper(), httpRequestHandler());
    }

    private HttpRequestMapper httpRequestMapper() {
        return new HttpRequestMapper(httpRequestParser());
    }

    private HttpRequestParser httpRequestParser() {
        return new HttpRequestParser();
    }

    private HttpRequestHandler httpRequestHandler() {
        return new HttpRequestHandler(handlerMapper(), staticResourceHandler(), zonedDateTimeGenerator());
    }

    private HandlerMapper handlerMapper() {
        return new HandlerMapper();
    }

    private StaticResourceHandler staticResourceHandler() {
        return new StaticResourceHandler(staticResourceReader(), mappingMediaTypeFileExtensionResolver());
    }

    private StaticResourceReader staticResourceReader() {
        return new StaticResourceReader();
    }

    private MappingMediaTypeFileExtensionResolver mappingMediaTypeFileExtensionResolver() {
        return new MappingMediaTypeFileExtensionResolver();
    }

    private ZonedDateTimeGenerator zonedDateTimeGenerator() {
        return new CurrentZonedDateTimeGenerator();
    }

}
