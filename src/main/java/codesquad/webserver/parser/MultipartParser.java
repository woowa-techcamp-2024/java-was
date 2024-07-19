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

public abstract class MultipartParser {
    private static final Logger logger = LoggerFactory.getLogger(MultipartParser.class);
    private static final String UTF_8 = "UTF-8";
    private static final String ISO_8859_1 = "ISO-8859-1";
    private static final int BUFFER_SIZE = 8192;
    private static final int MAX_FILE_SIZE = 100 * 1024 * 1024;

    public static void parse(BufferedInputStream in, HttpRequest request) throws IOException {
        String boundary = "--" + extractBoundary(request.getHeaders().get("Content-Type"));
        byte[] boundaryBytes = boundary.getBytes(ISO_8859_1);
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        long totalBytesRead = 0;

        Map<String, List<String>> multipartFields = new HashMap<>();
        Map<String, List<HttpRequest.FileItem>> multipartFiles = new HashMap<>();

        boolean isClosingBoundaryFound = false;
        ByteArrayOutputStream currentPart = new ByteArrayOutputStream();
        boolean inHeader = true;
        Map<String, String> currentHeaders = new HashMap<>();

        while ((bytesRead = in.read(buffer)) != -1) {
            logger.debug("Read {} bytes", bytesRead);
            totalBytesRead += bytesRead;
            if (totalBytesRead > MAX_FILE_SIZE) {
                throw new IOException("File size exceeds the maximum limit of " + MAX_FILE_SIZE + " bytes");
            }

            for (int i = 0; i < bytesRead; i++) {
                currentPart.write(buffer[i]);

                if (inHeader) {
                    if (endsWith(currentPart, "\r\n\r\n".getBytes())) {
                        inHeader = false;
                        currentHeaders = parseHeaders(
                                new String(currentPart.toByteArray(), ISO_8859_1));
                        currentPart.reset();
                    }
                } else if (endsWith(currentPart, boundaryBytes)) {
                    processPart(currentHeaders, currentPart.toByteArray(), boundaryBytes.length, multipartFields,
                            multipartFiles);
                    currentPart.reset();
                    inHeader = true;
                    currentHeaders.clear();

                    if (i + 2 < bytesRead && buffer[i + 1] == '-' && buffer[i + 2] == '-') {
                        isClosingBoundaryFound = true;
                        logger.info("Closing boundary found.");
                        break;
                    }
                }
            }

            if (isClosingBoundaryFound) {
                break;
            }
        }

        if (!isClosingBoundaryFound) {
            logger.warn("Parsing completed without finding closing boundary.");
            if (currentPart.size() > 0) {
                processPart(currentHeaders, currentPart.toByteArray(), 0, multipartFields, multipartFiles);
            }
        }

        request.setMultipartFields(multipartFields);
        request.setMultipartFiles(multipartFiles);
        logger.info("Finished parsing multipart data. Fields: {}, Files: {}", multipartFields.keySet(),
                multipartFiles.keySet());
    }

    private static void processPart(Map<String, String> headers, byte[] partData, int boundaryLength,
                                    Map<String, List<String>> fields, Map<String, List<HttpRequest.FileItem>> files)
            throws IOException {
        String contentDisposition = headers.get("Content-Disposition");
        String contentType = headers.get("Content-Type");

        if (contentDisposition == null) {
            logger.warn("Content-Disposition header not found in part.");
            return;
        }

        String name = extractAttribute(contentDisposition, "name");
        String filename = extractAttribute(contentDisposition, "filename");

        byte[] content = Arrays.copyOfRange(partData, 0,
                partData.length - boundaryLength - 2); // -2 for \r\n before boundary

        if (filename != null) {
            HttpRequest.FileItem fileItem = new HttpRequest.FileItem(filename, content);
            files.computeIfAbsent(name, k -> new ArrayList<>()).add(fileItem);
            logger.info("Saved file: name={}, filename={}, contentType={}, size={} bytes",
                    name, filename, contentType, content.length);
        } else {
            String value = new String(content, UTF_8).trim();
            fields.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
            logger.info("Saved field: name={}, value={}", name, value);
        }

        logger.debug("Processed part: name={}, filename={}, content length={}",
                name, filename, content.length);
    }

    private static boolean endsWith(ByteArrayOutputStream baos, byte[] suffix) {
        byte[] bytes = baos.toByteArray();
        if (bytes.length < suffix.length) {
            return false;
        }
        for (int i = 1; i <= suffix.length; i++) {
            if (bytes[bytes.length - i] != suffix[suffix.length - i]) {
                return false;
            }
        }
        return true;
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