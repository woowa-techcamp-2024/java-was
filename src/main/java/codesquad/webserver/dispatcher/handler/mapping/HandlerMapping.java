package codesquad.webserver.dispatcher.handler.mapping;

import codesquad.webserver.httprequest.HttpRequest;


public interface HandlerMapping {

    Object getHandler(HttpRequest request);
}
