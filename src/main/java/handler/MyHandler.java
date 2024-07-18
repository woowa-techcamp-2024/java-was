package handler;

import exception.MethodNotAllowed;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import http.startline.RequestLine;
import java.io.IOException;
import org.slf4j.Logger;
import util.LoggerUtil;

public abstract class MyHandler {
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

    void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException{
        ResponseValueSetter.failRedirect(httpResponse, new MethodNotAllowed());
    }
    void doPost(HttpRequest httpRequest, HttpResponse httpResponse){
        ResponseValueSetter.failRedirect(httpResponse, new MethodNotAllowed());
    }
}
