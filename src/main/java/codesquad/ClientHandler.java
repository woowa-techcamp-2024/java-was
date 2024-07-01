package codesquad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

class ClientHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket clientSocket;
    private final Router router;

    public ClientHandler(Socket socket, Router router) {
        this.clientSocket = socket;
        this.router = router;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream()
        ) {
            logger.debug("Client connected");
            String request = in.readLine();
            if (request != null && !request.isEmpty()) {
                String[] requestParts = request.split(" ");
                String method = requestParts[0];
                String url = requestParts[1];
                String protocol = requestParts[2];
                logger.debug("Client request HTTP method: {}", method);
                logger.debug("Client request URL: {}", url);
                logger.debug("Client request HTTP Protocol: {}", protocol);
                String content = router.getContent(url);
                sendResponse(out, content);
            }
        } catch (IOException e) {
            logger.error("Error handling client request", e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.error("Error closing client socket", e);
            }
        }
    }

    private void sendResponse(OutputStream out, String response) throws IOException {
        out.write("HTTP/1.1 200 OK\r\n".getBytes());
        out.write("Content-Type: text/html\r\n".getBytes());
        out.write("\r\n".getBytes());
        out.write(response.getBytes());
        out.flush();
    }
}
