package codesquad;

import codesquad.http.HttpRequest;
import codesquad.utils.ResourcesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;

public class HttpRequestHandler implements Runnable {

    private final Logger log = LoggerFactory.getLogger(HttpRequestHandler.class);

    private final Socket socket;

    public HttpRequestHandler(Socket socket) {
        this.socket = socket;
        log.debug("Client connected");
    }

    @Override
    public void run() {
        try {
            String request = readHttpRequest();
            log.debug("request = {}", request);
            HttpRequest httpRequest = HttpRequest.fromText(request);
            log.debug("httpRequest = {}", httpRequest);

            Optional<String> data = ResourcesReader.readResource("static" + httpRequest.uri());
            try (OutputStream clientOutput = socket.getOutputStream()) {
                data.ifPresentOrElse(text -> responseOK(clientOutput, text),
                        () -> responseNotFound(clientOutput));
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseOK(OutputStream clientOutput, String data) {
        try {
            clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
            clientOutput.write("Content-Type: text/html\r\n".getBytes());
            clientOutput.write("\r\n".getBytes());
            clientOutput.write(data.getBytes());
            clientOutput.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void responseNotFound(OutputStream clientOutput) {
        try {
            clientOutput.write("HTTP/1.1 404 Not Found\r\n".getBytes());
            clientOutput.write("Content-Type: text/html\r\n".getBytes());
            clientOutput.write("\r\n".getBytes());
            clientOutput.write("<h1>404 Not Found</h1>".getBytes());
            clientOutput.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String readHttpRequest() throws IOException {
        InputStream inputStream = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder request = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            request.append(line)
                    .append(System.lineSeparator());
        }
        return request.toString();
    }

}
