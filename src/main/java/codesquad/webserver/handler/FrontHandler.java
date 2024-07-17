package codesquad.webserver.handler;

import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpRequestParser;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.http.HttpResponseParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrontHandler implements Runnable{
    private final Logger log = LoggerFactory.getLogger(FrontHandler.class);

    private Socket connect;
    private List<ApiHandler> handlers;

    public FrontHandler(Socket connect, DynamicResourceHandler dynamicResourceHandler) {
        this.connect = connect;
        handlers = List.of(dynamicResourceHandler, new StaticResourceHandler());
    }

    @Override
    public void run() {
        log.debug("Client Connected IP: {}, Port: {}", connect.getInetAddress(), connect.getPort());
        try{
            InputStream inputStream = connect.getInputStream();
            HttpRequest request = HttpRequestParser.parse(inputStream);
            log.info("request: {}", request);
            HttpResponse response = handlers.stream()
                    .filter(handler -> handler.canHandle(request))
                    .findFirst()
                    .map(handler -> handler.handle(request))
                    .orElse(HttpResponse.badRequest());
            log.info("response: {}", response);
            sendResponse(response);
        }
        catch (IOException e){
            log.error("Error get input stream" + e.getMessage(), e);
            sendResponse(HttpResponse.serverError());
        }finally {
            try{
                connect.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void sendResponse(HttpResponse response) {
        try (OutputStream outputStream = connect.getOutputStream()){
            HttpResponseParser.parse(outputStream, response);
            outputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
