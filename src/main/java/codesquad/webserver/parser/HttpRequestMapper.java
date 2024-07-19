package codesquad.webserver.parser;

import codesquad.servlet.execption.ClientException;
import codesquad.servlet.execption.GlobalExceptionHandler;
import codesquad.servlet.execption.MethodNotAllowedException;
import codesquad.webserver.http.HttpHeaders;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpRequestLine;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.http.HttpStatus;
import codesquad.webserver.http.MultiPartFormData;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestMapper {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestMapper.class);

    private final HttpRequestParser httpRequestParser;
    private final GlobalExceptionHandler globalExceptionHandler;

    public HttpRequestMapper(HttpRequestParser httpRequestParser, GlobalExceptionHandler globalExceptionHandler) {
        this.httpRequestParser = httpRequestParser;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    public HttpRequest mapFrom(InputStream inputStream, HttpResponse httpResponse) {
        try {
            logger.debug("HttpRequestMapper start");
            HttpRequestLine httpRequestLine = httpRequestParser.parseHttpRequestFirstLine(inputStream);
            HttpHeaders httpHeaders = httpRequestParser.parseHeaders(inputStream);
            byte[] body = httpRequestParser.readBody(inputStream, httpHeaders.getContentLength());

            HttpRequest httpRequest = new HttpRequest(httpRequestLine, httpHeaders, body);

            String contentType = httpHeaders.getHeader("Content-Type");
            if (contentType != null && contentType.contains("multipart/form-data")) {
                String boundaryInfo = contentType.split("; ")[1];
                String boundary = boundaryInfo.split("=")[1];
                inputStream = new ByteArrayInputStream(body);
                Map<String, MultiPartFormData> multiPartFormData = MultiPartFormParser.parse(inputStream, boundary);
                httpRequest.setMultiPartFormData(multiPartFormData);

                Map<String, String> params = new HashMap<>();
                for (String name : multiPartFormData.keySet()) {
                    if (name.equals("image")) {
                        continue;
                    }
                    String value = new String(multiPartFormData.get(name).getContent(), StandardCharsets.UTF_8);
                    params.put(name, value);
                }
                httpRequest.setParams(params);
            }

            if (contentType != null && contentType.contains("application/x-www-form-urlencoded")) {
                Map<String, String> params = httpRequestParser.parseBody(httpRequest.getBody(),
                        httpHeaders.getContentLength());

                httpRequest.setParams(params);
            }

            return httpRequest;

        } catch (MethodNotAllowedException e) {
            globalExceptionHandler.handle(e, httpResponse);
            throw e;
        } catch (Exception e) {
            ClientException clientException = new ClientException(e, HttpStatus.BAD_REQUEST);
            globalExceptionHandler.handle(clientException, httpResponse);
            throw clientException;
        }
    }

}
