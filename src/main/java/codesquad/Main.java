package codesquad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 10; // 스레드 풀 크기 설정

    public static void main(String[] args) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.debug("Listening for connection on port {} ....", PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.execute(new ClientHandler(clientSocket));
            }
        } finally {
            executor.shutdown();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    OutputStream clientOutput = clientSocket.getOutputStream()
            ) {
                logger.debug("Client connected");
                String request = in.readLine();
                if (request != null && !request.isEmpty()) {
                    String[] requestParts = request.split(" ");
                    logger.debug("Client request HTTP method: {}", requestParts[0]);
                    logger.debug("Client request URL: {}", requestParts[1]);
                    logger.debug("Client request HTTP Protocol: {}", requestParts[2]);
                }

                // HTTP 응답 생성
                clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
                clientOutput.write("Content-Type: text/html\r\n".getBytes());
                clientOutput.write("\r\n".getBytes());
                clientOutput.write("<h1>Hello</h1>\r\n".getBytes());
                clientOutput.flush();
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
    }
}