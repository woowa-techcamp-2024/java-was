package codesquad.webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final HttpParser httpParser;
    private final FileReader fileReader;
    private final HttpResponseBuilder responseBuilder;

    public RequestHandler() {
        this.httpParser = new HttpParser();
        this.fileReader = new FileReader();
        this.responseBuilder = new HttpResponseBuilder();
    }

    public void handle(Socket clientSocket) throws IOException {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream()
        ) {
            HttpRequest request = httpParser.parse(in);
            logger.debug("Received request: " + request.method() + " " + request.path());

            File file = fileReader.read(request.path());
            HttpResponse response = responseBuilder.build(file);

            sendResponse(out, response);
        }
    }

    private void sendResponse(OutputStream out, HttpResponse response) throws IOException {
        out.write(response.getByte());
        out.flush();
    }
}
