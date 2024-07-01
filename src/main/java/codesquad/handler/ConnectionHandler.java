package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.enums.StatusCode;
import java.net.Socket;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionHandler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ConnectionHandler.class);
    private final Socket cilentSocket;

    public ConnectionHandler(Socket cilentSocket) {
        this.cilentSocket = cilentSocket;
    }

    @Override
    public void run() {
        try (var is = cilentSocket.getInputStream();
            var os = cilentSocket.getOutputStream()) {
            log.debug("Client connected");
            var request = HttpRequest.from(is);
            var response = HttpResponse.of("HTTP/1.1", StatusCode.OK,
                Map.of("Content-Type", "text/html"), request.getRequestUri());
            os.write(response.generateResponse());
            os.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
