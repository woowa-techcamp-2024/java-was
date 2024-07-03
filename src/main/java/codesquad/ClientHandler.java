package codesquad;

import http.Http;
import http.startline.RequestLine;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import org.slf4j.Logger;
import util.FilePath;
import util.LoggerUtil;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final Logger logger = LoggerUtil.getLogger();
    private final String rootPath = System.getProperty("user.dir");
    private final String staticPath = rootPath + "/src/main/resources/static";

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
            OutputStream clientOutput = clientSocket.getOutputStream();
            InputStream inputStream = clientSocket.getInputStream()
        ) {
            // HTTP 응답을 생성합니다.
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Http http = Http.generate(bufferedReader);
            RequestLine requestLine = (RequestLine) http.getStartLine();
            // .xx 이 있으면 static 파일로 처리, 없으면 controller로 처리
            logger.info("Request: {}", http);
            // resource/static/index.html 파일을 읽어서 보내기
            FilePath filePath = new FilePath(staticPath);
            File file = new File(filePath.join(requestLine.getPath().getPath()).getPath());

            if (file.exists()) {
                logger.info("file exists");

                String contentType = guessContentType(file);
                if (contentType == null) {
                    contentType = "application/octet-stream"; // 기본 MIME 타입
                }

                // 파일의 모든 바이트 읽기
                byte[] fileContent = new byte[(int) file.length()];
                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                    bis.read(fileContent, 0, fileContent.length);
                }

                // HTTP 응답 헤더 생성
                String httpResponseHeader = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "Content-Length: " + fileContent.length + "\r\n" +
                    "\r\n";
                clientOutput.write(httpResponseHeader.getBytes());
                clientOutput.write(fileContent);
                clientOutput.flush();
            } else {
                logger.info("file not exists");
            }
            //clientOutput

        } catch (IOException e) {
            logger.error("Error handling client: {}", e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.error("Error closing client socket: {}", e.getMessage());
            }
        }
    }

    private String guessContentType(File file) {
        String fileName = file.getName();
        String extension = getFileExtension(fileName);

        return switch (extension) {
            case "html", "htm" -> "text/html";
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "css" -> "text/css";
            case "js" -> "application/javascript";
            case "json" -> "application/json";
            case "svg" -> "image/svg+xml";
            default -> "application/octet-stream"; // 기타 파일 형식에 대한 기본 MIME 타입
        };
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex == -1) {
            return ""; // 확장자가 없는 경우 빈 문자열 반환
        }
        return fileName.substring(lastIndex + 1).toLowerCase();
    }


}
