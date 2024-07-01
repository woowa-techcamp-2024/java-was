package codesquad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientRequest implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ClientRequest.class);

    private final Socket clientSocket;

    public ClientRequest(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            log.debug("Client connected");

            // HTTP 요청을 파싱합니다.
            InputStream clientInput = clientSocket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(clientInput));
            StringBuilder sb = new StringBuilder();
            String readLine;
            while (!(readLine = br.readLine()).isEmpty()) {
                sb.append(readLine).append("\n");
            }
            HttpRequestMessage requestMessage = HttpRequestMessage.parse(sb.toString());
            log.debug("Http request message={}", requestMessage);

            // HTTP 응답을 생성합니다.
            OutputStream clientOutput = clientSocket.getOutputStream();
            clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
            clientOutput.write("Content-Type: text/html\r\n".getBytes());
            clientOutput.write("\r\n".getBytes());
            clientOutput.write("<h1>Hello</h1>\r\n".getBytes()); // 응답 본문으로 "Hello"를 보냅니다.
            clientOutput.flush();

            clientSocket.close();
        } catch (IOException e) {
            log.warn("입출력 예외가 발생했습니다.", e);
        }
    }
}
