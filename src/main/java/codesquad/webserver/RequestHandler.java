package codesquad.webserver;

import codesquad.handler.Handler;
import codesquad.handler.HandlerMapping;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler {
    private final Socket connect;
    private final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public RequestHandler(Socket connect) {
        this.connect = connect;
    }

    public void handle(){
        log.debug("Client Connected IP: {}, Port: {}", connect.getInetAddress(), connect.getPort());
        try{
            InputStream inputStream = connect.getInputStream();
            OutputStream outputStream = connect.getOutputStream();
            HttpRequest request = new HttpRequest(inputStream);
            HttpResponse response = new HttpResponse(outputStream);
            Handler handler = HandlerMapping.getHandler(request.getPath());
            if(handler == null){
            } else{
                log.debug("call handle");
                handler.handle(request, response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
