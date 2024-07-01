package codesquad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Listening for connection on port 8080 ....");

        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Client connected");

                InputStream clientInput = clientSocket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(clientInput);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = bufferedReader.readLine();
                String[] words = line.split(" ");
                HttpRequest httpRequest = new HttpRequest(words[0], words[1], words[2]);
                logger.debug(httpRequest.toString());

                while (true) {
                    line = bufferedReader.readLine();
                    if (line.isEmpty()) break;
                    httpRequest.setHeader(line);
                }

                if (Objects.equals(httpRequest.getUri(), "/index.html")) {
                    OutputStream clientOutput = clientSocket.getOutputStream();
                    clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
                    clientOutput.write("Content-Type: text/html\r\n".getBytes());
                    clientOutput.write("\r\n".getBytes());

                    int data = 0;
                    FileInputStream fileInputStream = new FileInputStream("src/main/resources/static/index.html");
                    byte[] buffer = new byte[fileInputStream.available()];
                    while ((data = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
                        clientOutput.write(new String(buffer, 0, data).getBytes());
                    }
                    clientOutput.flush();
                }
            }
        }
    }
}
