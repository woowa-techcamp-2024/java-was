package codesquad.webserver.http;

import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestParser {
    private static final Logger log = LoggerFactory.getLogger(HttpRequestParser.class);

    public static HttpRequest parse(InputStream inputStream) {
        // BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        HttpRequestStartLine startLine = new HttpRequestStartLine(readStartLine(inputStream));
        log.info("[parse] start line " + startLine);
        HttpHeader header = readHeader(inputStream);
        log.info("[parse] header " + header);
        HttpBody body = null;

        if (header.hasKey("Content-Type") && header.hasKey("Content-Length")) {
            String contentType = header.getValue("Content-Type");
            int contentLength = Integer.parseInt(header.getValue("Content-Length"));
            if (contentType.startsWith("application/x-www-form-urlencoded")) {
                body = readUrlEncodedForm(inputStream, contentLength);
            } else if (contentType.startsWith("multipart/form-data")) {
                byte[] bodyBytes = readBytes(inputStream, contentLength);
                body = readMultipartFormData(bodyBytes, contentType);
            }
        }
        log.info("[parse] body " + body);
        return new HttpRequest(startLine, header, body);
    }

    private static byte[] readBytes(InputStream inputStream, int length) {
        try {
            byte[] buffer = new byte[length];
            int bytesRead = 0;
            while (bytesRead < length) {
                int result = inputStream.read(buffer, bytesRead, length - bytesRead);
                if (result == -1)
                    break;
                bytesRead += result;
                log.debug("[readBytes]: {}/{}", bytesRead, length);
            }
            return buffer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readLine(InputStream inputStream) {
        try {
            StringBuilder line = new StringBuilder();
            int nextChar;
            while ((nextChar = inputStream.read()) != -1) {
                if (nextChar == '\r') {
                    nextChar = inputStream.read(); // Skip '\n'
                    break;
                }
                if (nextChar == '\n') {
                    break;
                }
                line.append((char) nextChar);
            }
            return line.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static String readStartLine(InputStream inputStream) {
        return readLine(inputStream);
    }

    private static HttpHeader readHeader(InputStream inputStream) {
        HttpHeader headers = new HttpHeader();
        String line;
        // \r\n 까지 읽는다
        while (!(line = readLine(inputStream)).isEmpty()) {
            String[] row = line.split(":\\s*", 2);
            headers.add(row[0], row[1]);
        }
        return headers;
    }

    private static HttpBody readUrlEncodedForm(InputStream inputStream, int contentLength) {
        String mesage = new String(readBytes(inputStream, contentLength));
        return new HttpBody(HttpRequestBodyParser.parseForm(mesage));
    }

    private static HttpBody readMultipartFormData(byte[] bodyBytes, String contentType) {
        Map<String, Object> formData = new HashMap<>();
        String boundary = contentType.split("boundary=", 2)[1].trim();
        log.debug("[readMultipartFormData] boundary " + boundary);
        byte[] delimiter = ("--" + boundary).getBytes();
        byte[] endDelimiter = ("--" + boundary + "--").getBytes();
        byte[] newLine = ("\r\n").getBytes();
        int start = 0;
        int delimiterPos = indexOf(bodyBytes, delimiter, start);
        int endDelimiterPos = indexOf(bodyBytes, endDelimiter, start);
        while (delimiterPos != endDelimiterPos) {
            log.debug("[readMultipartFormData] delemiterPost/endDelimiterPos {}:{}", delimiterPos, endDelimiterPos);
            start = delimiterPos + delimiter.length + newLine.length; // "--bodunary\r\n" 다음 위치
            delimiterPos = indexOf(bodyBytes, delimiter, start); // 다음 줄의 "--bodunary\r\n" 첫 위치
            int newLinePos = indexOf(bodyBytes, newLine, start); // content-disposition가 있는 줄에 "\r\n" 첫 위치
            String contentDisposition = new String(Arrays.copyOfRange(bodyBytes, start, newLinePos));
            log.debug("[readMultipartFormData] content-disposition: " + contentDisposition);
            if (contentDisposition.contains("filename=")) {
                // 파일 처리
                String name = getFieldName(contentDisposition.split(";")[1]);
                String filename = getFileName(contentDisposition.split(";")[2]);
                int contentTypeNewLinePos = indexOf(bodyBytes, newLine, newLinePos + newLine.length); // content-type \r\n 첫 위치
                String partContentType = new String(Arrays.copyOfRange(bodyBytes, newLinePos + newLine.length, contentTypeNewLinePos));
                byte[] fileBytes = Arrays.copyOfRange(bodyBytes, contentTypeNewLinePos + newLine.length * 2, delimiterPos - newLine.length);
                log.debug("[readMultipartFormData] filename: " + filename + ", part-content-type: " + partContentType + ", file size: " + fileBytes.length + "byte");
                formData.put(name, fileBytes);
                formData.put("filename", UUID.randomUUID().toString() + filename.substring(filename.lastIndexOf(".")));
            } else {
                // 문자열 처리
                String name = getFieldName(contentDisposition.split(";")[1]);
                String data = new String(Arrays.copyOfRange(bodyBytes, newLinePos + newLine.length * 2, delimiterPos - newLine.length));
                log.debug("[readMultipartFormData] name: " + name + ", data: " + data);
                formData.put(name, data);
            }
        }
        return new HttpBody(formData);
    }

    private static int indexOf(byte[] origin, byte[] target, int start) {
        for (int i = start; i < origin.length - target.length; i++) {
            boolean found = true;
            for (int j = 0; j < target.length; j++) {
                if (origin[i + j] != target[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    private static String getFieldName(String contentDisposition) {
        return contentDisposition.substring(contentDisposition.indexOf("name=") + "name=".length()).replaceAll("\"", "").trim();
    }

    private static String getFileName(String contentDisposition) {
        return contentDisposition.substring(contentDisposition.indexOf("filename=") + "filename=".length()).replaceAll("\"", "").trim();
    }
}
