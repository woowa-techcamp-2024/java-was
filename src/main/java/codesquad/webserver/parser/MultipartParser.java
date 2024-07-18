package codesquad.webserver.parser;

import codesquad.webserver.httprequest.HttpRequest;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipartParser {
    private static final Logger logger = LoggerFactory.getLogger(MultipartParser.class);
    private static final String UTF_8 = "UTF-8";
    private static final String ISO_8859_1 = "ISO-8859-1";
    private static final int BUFFER_SIZE = 1024 * 1024;

    public static void parse(BufferedInputStream in, HttpRequest request) throws IOException {
        String boundary = "--" + extractBoundary(request.getHeaders().get("Content-Type"));
        byte[] boundaryBytes = boundary.getBytes(ISO_8859_1);
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Map<String, List<String>> multipartFields = new HashMap<>();
        Map<String, List<HttpRequest.FileItem>> multipartFiles = new HashMap<>();

        boolean isClosingBoundaryFound = false;

        while ((bytesRead = in.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
            isClosingBoundaryFound = processParts(outputStream, boundaryBytes, multipartFields, multipartFiles);
            if (isClosingBoundaryFound) {
                logger.info("Closing boundary found. Stopping parse process.");
                break;
            }
        }

        if (!isClosingBoundaryFound) {
            logger.warn("Parsing completed without finding closing boundary.");
        }

        request.setMultipartFields(multipartFields);
        request.setMultipartFiles(multipartFiles);
        logger.info("Finished parsing multipart data. Fields: {}, Files: {}", multipartFields.keySet(),
                multipartFiles.keySet());
    }

    private static boolean processParts(ByteArrayOutputStream outputStream, byte[] boundaryBytes,
                                        Map<String, List<String>> fields, Map<String, List<HttpRequest.FileItem>> files)
            throws IOException {
        byte[] data = outputStream.toByteArray();
        int startPos = 0;
        int boundaryPos;

        while ((boundaryPos = findBoundary(data, boundaryBytes, startPos)) != -1) {
            if (startPos != 0) {
                processPart(Arrays.copyOfRange(data, startPos, boundaryPos), fields, files);
            }
            startPos = boundaryPos + boundaryBytes.length + 2; // +2 to skip \r\n after boundary

            // Check for closing boundary
            if (boundaryPos + boundaryBytes.length + 2 < data.length &&
                    data[boundaryPos + boundaryBytes.length] == '-' &&
                    data[boundaryPos + boundaryBytes.length + 1] == '-') {
                logger.info("Closing boundary found in processParts");
                outputStream.reset(); // Clear the buffer as we've reached the end
                return true;
            }
        }

        outputStream.reset();
        if (startPos < data.length) {
            outputStream.write(data, startPos, data.length - startPos);
        }
        return false;
    }

    private static void processPart(byte[] partData, Map<String, List<String>> fields,
                                    Map<String, List<HttpRequest.FileItem>> files) throws IOException {
        int headerEnd = findSequence(partData, "\r\n\r\n".getBytes(ISO_8859_1));
        if (headerEnd == -1) {
            logger.warn("Invalid part format. Headers not found.");
            return;
        }

        String headerContent = new String(partData, 0, headerEnd, ISO_8859_1);
        Map<String, String> headers = parseHeaders(headerContent);
        String contentDisposition = headers.get("Content-Disposition");
        String contentType = headers.get("Content-Type");

        if (contentDisposition == null) {
            logger.warn("Content-Disposition header not found in part.");
            return;
        }

        String name = extractAttribute(contentDisposition, "name");
        String filename = extractAttribute(contentDisposition, "filename");

        byte[] content = Arrays.copyOfRange(partData, headerEnd + 4, partData.length);

        if (filename != null) {
            HttpRequest.FileItem fileItem = new HttpRequest.FileItem(filename, content);
            files.computeIfAbsent(name, k -> new ArrayList<>()).add(fileItem);
            logger.info("Saved file: name={}, filename={}, contentType={}, size={} bytes", name, filename, contentType,
                    content.length);
        } else {
            String value = new String(content, UTF_8).trim();
            fields.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
            logger.info("Saved field: name={}, value={}", name, value);
        }
    }

    private static int findBoundary(byte[] data, byte[] boundary, int startPos) {
        for (int i = startPos; i <= data.length - boundary.length; i++) {
            if (compareBoundary(data, i, boundary)) {
                return i;
            }
        }
        return -1;
    }

    private static boolean compareBoundary(byte[] data, int start, byte[] boundary) {
        for (int i = 0; i < boundary.length; i++) {
            if (data[start + i] != boundary[i]) {
                return false;
            }
        }
        return true;
    }

    private static int findSequence(byte[] data, byte[] sequence) {
        for (int i = 0; i <= data.length - sequence.length; i++) {
            boolean found = true;
            for (int j = 0; j < sequence.length; j++) {
                if (data[i + j] != sequence[j]) {
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

    private static Map<String, String> parseHeaders(String headerContent) {
        Map<String, String> headers = new HashMap<>();
        for (String line : headerContent.split("\r\n")) {
            int colonIndex = line.indexOf(':');
            if (colonIndex > 0) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                headers.put(key, value);
            }
        }
        return headers;
    }

    private static String extractAttribute(String header, String attribute) {
        int start = header.indexOf(attribute + "=\"");
        if (start != -1) {
            int end = header.indexOf("\"", start + attribute.length() + 2);
            if (end != -1) {
                return header.substring(start + attribute.length() + 2, end);
            }
        }
        return null;
    }

    private static String extractBoundary(List<String> contentTypes) {
        if (contentTypes == null || contentTypes.isEmpty()) {
            throw new IllegalArgumentException("Content-Type header not found");
        }
        String contentType = contentTypes.get(0);
        String[] parts = contentType.split(";");
        for (String part : parts) {
            if (part.trim().startsWith("boundary=")) {
                return part.split("=", 2)[1].trim().replaceAll("^\"|\"$", "");
            }
        }
        throw new IllegalArgumentException("Boundary not found in Content-Type");
    }
}