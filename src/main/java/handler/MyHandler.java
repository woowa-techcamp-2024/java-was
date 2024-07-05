package handler;

import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import http.startline.RequestLine;
import java.io.IOException;
import org.slf4j.Logger;
import util.LoggerUtil;

abstract public class MyHandler {
    private static final Logger logger = LoggerUtil.getLogger();

    public void handle(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        RequestLine requestLine = (RequestLine) httpRequest.getStartLine();
        HttpMethod httpMethod = requestLine.getMethod();
        logger.info("Request: {}", httpRequest);
        if (httpMethod.equals(HttpMethod.GET)) {
            logger.info("GET");
            doGet(httpRequest, httpResponse);
        } else if (httpMethod.equals(HttpMethod.POST)) {
            logger.info("POST");
            doPost(httpRequest, httpResponse);
        }
    }

    abstract void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
    abstract void doPost(HttpRequest httpRequest, HttpResponse httpResponse);
}
