package codesquad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketAccepter implements Runnable {
    private final ServerSocket serverSocket;
    private final Logger log = LoggerFactory.getLogger(Main.class);

    public SocketAccepter(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                log.info("Client connected: {}", clientSocket.getRemoteSocketAddress());

                InputStream inputStream = clientSocket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(inputStreamReader);

                StringBuilder request = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.isEmpty()) {
                        break;
                    }
                    request.append(line).append("\n");
                }
                log.info("Request: {}", request);

                clientSocket.getOutputStream().write("HTTP/1.1 200 OK\r\n".getBytes());
                clientSocket.getOutputStream().write("Content-Type: text/html\r\n".getBytes());
                clientSocket.getOutputStream().write("\r\n".getBytes());
                clientSocket.getOutputStream().write("<h1>Hello, World!</h1>".getBytes());
                clientSocket.getOutputStream().flush();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
