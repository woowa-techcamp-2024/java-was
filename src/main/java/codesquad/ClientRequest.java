package codesquad;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;
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
            String rawHttpRequestMessage = readHttpRequestMessage(clientInput);
            HttpRequestMessage requestMessage = HttpRequestMessage.parse(rawHttpRequestMessage);
            log.debug("Http request message={}", requestMessage);

            // 정적 파일의 경로를 찾습니다.
            String rawHttpResponseMessage = getStaticFile("static/" + requestMessage.requestUrl());

            // 컨텐츠 타입을 매핑합니다.
            int extensionStartIndex = requestMessage.requestUrl().indexOf(".");
            String fileNameExtension = requestMessage.requestUrl().substring(extensionStartIndex + 1);
            String contentType = ContentTypes.getMimeType(fileNameExtension);

            // HTTP 응답을 생성합니다.
            OutputStream clientOutput = clientSocket.getOutputStream();
            clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
            String contentTypeHeader = MessageFormat.format("Content-Type: {0}\r\n", contentType);
            clientOutput.write(contentTypeHeader.getBytes());
            clientOutput.write("\r\n".getBytes());
            clientOutput.write(rawHttpResponseMessage.getBytes()); // 응답 본문으로 "Hello"를 보냅니다.
            clientOutput.flush();

            clientSocket.close();
        } catch (IOException e) {
            log.warn("입출력 예외가 발생했습니다.", e);
        }
    }

    private static String readHttpRequestMessage(InputStream clientInput) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(clientInput));
        StringBuilder sb = new StringBuilder();
        String readLine;
        while (!(readLine = br.readLine()).isEmpty()) {
            sb.append(readLine).append("\n");
        }
        return sb.toString();
    }

    private String getStaticFile(String resourcePath) throws IOException {
        URL resource = getClass().getClassLoader().getResource(resourcePath);
        Path path = new File(resource.getPath()).toPath();
        List<String> contents = Files.readAllLines(path);
        StringBuilder sb = new StringBuilder();
        for (String content : contents) {
            sb.append(content).append("\n");
        }
        return sb.toString();
    }
}
