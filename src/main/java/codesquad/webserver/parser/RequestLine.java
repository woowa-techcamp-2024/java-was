package codesquad.webserver.parser;

import codesquad.webserver.parser.enums.HttpMethod;

public record RequestLine(HttpMethod method, String path, String fullPath, String httpVersion) {

}
