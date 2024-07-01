package codesquad;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.enums.StatusCode;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080); // 8080 포트에서 서버를 엽니다.
        log.info("Listening for connection on port 8080 ....");

        while (true) {
            try (Socket clientSocket = serverSocket.accept();
                var is = clientSocket.getInputStream();
                var os = clientSocket.getOutputStream()) {
                log.info("Client connected");
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

}
