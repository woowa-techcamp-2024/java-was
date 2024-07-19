package codesquad.http.parser;

import codesquad.http.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MultipartParser {

    public static Map<String, Object> parse(byte[] body, String contentType) throws IOException {
        Map<String, Object> result = new HashMap<>();

        String boundary = extractBoundary(contentType);
        if (boundary == null) {
            throw new IOException("Boundary not found in Content-Type");
        }

        byte[] boundaryBytes = ("--" + boundary).getBytes();
        byte[] doubleCRLF = "\r\n\r\n".getBytes();

        int start = 0;
        while (start < body.length) {
            start = indexOf(body, boundaryBytes, start);
            if (start < 0) {
                break;
            }
            start += boundaryBytes.length;

            int end = indexOf(body, boundaryBytes, start);
            if (end < 0) {
                end = body.length;
            }

            int headerEnd = indexOf(body, doubleCRLF, start);
            if (headerEnd < 0 || headerEnd > end) {
                break;
            }
            headerEnd += doubleCRLF.length;

            byte[] headersPart = new byte[headerEnd - start];
            System.arraycopy(body, start, headersPart, 0, headersPart.length);

            Map<String, String> headers = extractHeaders(headersPart);
            String contentDisposition = headers.get("Content-Disposition");
            if (contentDisposition != null) {
                String name = extractName(contentDisposition);
                String filename = extractFilename(contentDisposition);
                if (name != null) {
                    int contentStart = headerEnd;
                    int contentEnd = end - 2;

                    byte[] content = new byte[contentEnd - contentStart];
                    System.arraycopy(body, contentStart, content, 0, content.length);

                    if (filename != null) {
                        String contentTypeHeader = headers.get("Content-Type");
                        MultipartFile multipartFile = new MultipartFile(name, filename, contentTypeHeader, content);
                        result.put(name, multipartFile);
                    } else {
                        result.put(name, new String(content));
                    }
                }
            }
            start = end;
        }

        return result;
    }

    private static String extractBoundary(String contentType) {
        String[] parts = contentType.split(";");
        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("boundary=")) {
                return part.substring("boundary=".length());
            }
        }
        return null;
    }

    private static int indexOf(byte[] data, byte[] pattern, int start) {
        outer:
        for (int i = start; i <= data.length - pattern.length; i++) {
            for (int j = 0; j < pattern.length; j++) {
                if (data[i + j] != pattern[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

    private static Map<String, String> extractHeaders(byte[] headersPart) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String[] headerLines = getHeaderLines(headersPart);
        for (String line : headerLines) {
            int colonIndex = line.indexOf(":");
            if (colonIndex != -1) {
                String name = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                headers.put(name, value);
            }
        }
        return headers;
    }

    private static String[] getHeaderLines(byte[] headersPart) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(headersPart);
        byte[] lineBuffer = new byte[1024];
        int bytesRead;
        StringBuilder headersBuilder = new StringBuilder();
        while ((bytesRead = inputStream.read(lineBuffer)) != -1) {
            headersBuilder.append(new String(lineBuffer, 0, bytesRead));
            if (headersBuilder.toString().contains("\r\n\r\n")) {
                break;
            }
        }
        return headersBuilder.toString().split("\r\n");
    }

    private static String extractName(String contentDisposition) {
        String[] parts = contentDisposition.split(";");
        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("name=")) {
                return part.substring("name=".length()).replace("\"", "");
            }
        }
        return null;
    }

    private static String extractFilename(String contentDisposition) {
        String[] parts = contentDisposition.split(";");
        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("filename=")) {
                return part.substring("filename=".length()).replace("\"", "");
            }
        }
        return null;
    }
}
