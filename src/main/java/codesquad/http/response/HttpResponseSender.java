package codesquad.http.response;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpResponseSender {

    /**
     * 클라이언트에게 HTTP 응답을 전송합니다.
     *
     * @param response     전송할 HTTP 응답 객체
     * @param clientOutput 클라이언트의 OutputStream
     * @throws IOException 전송 중 발생할 수 있는 IO 예외
     */
    public static void sendHttpResponse(HttpResponse response, OutputStream clientOutput) throws IOException {
        // HTTP 상태 라인 생성 (예: HTTP/1.1 200 OK)
        String statusLine = "HTTP/1.1 " + response.getStatusCode() + " " + response.getStatusMessage() + "\r\n";

        // HTTP 헤더 생성
        StringBuilder headersBuilder = new StringBuilder();
        for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
            headersBuilder.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }
        headersBuilder.append("\r\n"); // 헤더와 바디 구분

        // HTTP 본문 생성
        String body = response.getBody();

        // HTTP 응답 전체 생성
        String httpResponse = statusLine + headersBuilder.toString() + body;

        // UTF-8 인코딩으로 바이트 배열로 변환하여 전송
        clientOutput.write(httpResponse.getBytes(StandardCharsets.UTF_8));
        clientOutput.flush(); // 버퍼 비우기 (필요한 경우)

        // 이후에 클라이언트Output닫기
    }

    /**
     * HTML 파일을 HTTP 응답으로 전송합니다.
     *
     * @param outputStream 클라이언트의 OutputStream
     * @param status       HTTP 상태 코드 및 메시지
     * @param contentType  Content-Type 헤더 값
     * @param content      HTML 파일의 바이트 배열
     * @throws IOException 전송 중 발생할 수 있는 IO 예외
     */
    public static void sendHtmlResponse(OutputStream outputStream, String status, String contentType, byte[] content) throws IOException {
        String statusLine = "HTTP/1.1 " + status + "\r\n";
        String headers = "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + content.length + "\r\n"
                + "\r\n";

        outputStream.write(statusLine.getBytes(StandardCharsets.UTF_8));
        outputStream.write(headers.getBytes(StandardCharsets.UTF_8));
        outputStream.write(content);
        outputStream.flush();
    }

    /**
     * HTTP 응답을 생성하고 클라이언트에게 보냅니다.
     *
     * @param outputStream 클라이언트의 OutputStream
     * @param status       HTTP 상태 코드 및 메시지
     * @param contentType  Content-Type 헤더 값
     * @param content      응답 본문의 바이트 배열
     * @throws IOException 전송 중 발생할 수 있는 IO 예외
     */
    public static void sendHttpResponse(OutputStream outputStream, String status, String contentType, byte[] content) throws IOException {
        String statusLine = "HTTP/1.1 " + status + "\r\n";
        String headers = "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + content.length + "\r\n"
                + "\r\n";

        outputStream.write(statusLine.getBytes(StandardCharsets.UTF_8));
        outputStream.write(headers.getBytes(StandardCharsets.UTF_8));
        outputStream.write(content);
        outputStream.flush();
    }

    /**
     * HTTP 이미지 응답을 생성하고 클라이언트에게 보냅니다.
     *
     * @param outputStream 클라이언트의 OutputStream
     * @param status       HTTP 상태 코드 및 메시지
     * @param contentType  Content-Type 헤더 값
     * @param content      이미지 파일의 바이트 배열
     * @throws IOException 전송 중 발생할 수 있는 IO 예외
     */
    public static void sendImageResponse(OutputStream outputStream, String status, String contentType, byte[] content) throws IOException {
        String statusLine = "HTTP/1.1 " + status + "\r\n";
        String headers = "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + content.length + "\r\n"
                + "\r\n";

        outputStream.write(statusLine.getBytes(StandardCharsets.UTF_8));
        outputStream.write(headers.getBytes(StandardCharsets.UTF_8));
        outputStream.write(content);
        outputStream.flush();
    }
}

