package codesquad.webserver.httprequest;

import codesquad.webserver.parser.RequestLine;
import java.util.Map;

public record HttpRequest(RequestLine requestLine, Map<String, String> headers, Map<String, String> params, String body) {

}
