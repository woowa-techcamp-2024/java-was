package codesquad;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080); // 8080 포트에서 서버를 엽니다.
        log.debug("Listening for connection on port 8080 ....");

        while (true) {
            try (Socket clientSocket = serverSocket.accept();
                var br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                var bw = new BufferedWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream()))) {
                log.debug("Client connected");
                readRequest(br);

                bw.write(generateResponse());
                bw.flush();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private static void readRequest(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.isBlank()) {
                break;
            }
            log.debug(line);
        }
    }

    private static String generateResponse() {
        return "HTTP/1.1 200 OK\r\n"
            + "Content-Type: text/html\r\n"
            + "\r\n"
            + readIndexHTML();
    }

    private static String readIndexHTML() {
        var sb = new StringBuilder();
        try (var br = new BufferedReader(
            new InputStreamReader(new FileInputStream(
                "src/main/resources/static/index.html")))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return sb.toString();
    }
}
