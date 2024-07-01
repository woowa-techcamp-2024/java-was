package codesquad;

import codesquad.http.HttpRequest;
import codesquad.reader.FileByteReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
                var bw = new BufferedWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream()))) {
                log.info("Client connected");
                var request = HttpRequest.from(is);

                bw.write(generateResponse());
                bw.flush();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private static String generateResponse() {
        var fileByteReader = new FileByteReader("/index.html");
        return "HTTP/1.1 200 OK\r\n"
            + "Content-Type: text/html\r\n"
            + "\r\n"
            + new String(fileByteReader.readAllBytes());
    }

}
