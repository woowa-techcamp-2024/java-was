package codesquad.webserver.parser;

import codesquad.webserver.httprequest.HttpRequest;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MultipartParser {

    public static void parse(BufferedReader reader, HttpRequest request) throws IOException {
        String boundary = extractBoundary(request.getHeaders().get("Content-Type"));
        Map<String, List<String>> multipartFields = new HashMap<>();
        Map<String, List<HttpRequest.FileItem>> multipartFiles = new HashMap<>();

        String line;
        boolean isReadingPart = false;
        String currentName = null;
        String currentFilename = null;
        ByteArrayOutputStream currentContent = new ByteArrayOutputStream();

        while ((line = reader.readLine()) != null) {
            if (line.startsWith(boundary)) {
                if (isReadingPart) {
                    savePart(currentName, currentFilename, currentContent, multipartFields, multipartFiles);
                }
                isReadingPart = true;
                currentName = null;
                currentFilename = null;
                currentContent = new ByteArrayOutputStream();
            } else if (isReadingPart) {
                if (line.startsWith("Content-Disposition:")) {
                    currentName = extractAttribute(line, "name");
                    currentFilename = extractAttribute(line, "filename");
                } else if (line.isEmpty()) {
                    // Start of content
                } else {
                    currentContent.write(line.getBytes(StandardCharsets.UTF_8));
                    currentContent.write("\r\n".getBytes(StandardCharsets.UTF_8));
                }
            }

            if (line.equals(boundary + "--")) {
                break;
            }
        }

        request.setMultipartFields(multipartFields);
        request.setMultipartFiles(multipartFiles);
    }

    private static void savePart(String name, String filename, ByteArrayOutputStream content,
                                 Map<String, List<String>> fields, Map<String, List<HttpRequest.FileItem>> files) {
        if (filename != null) {
            files.computeIfAbsent(name, k -> new ArrayList<>()).add(
                    new HttpRequest.FileItem(filename, content.toByteArray())
            );
        } else {
            fields.computeIfAbsent(name, k -> new ArrayList<>()).add(
                    new String(content.toByteArray(), StandardCharsets.UTF_8).trim()
            );
        }
    }

    private static String extractBoundary(List<String> contentTypes) {
        if (contentTypes == null || contentTypes.isEmpty()) {
            throw new IllegalArgumentException("Content-Type header not found");
        }
        String contentType = contentTypes.get(0);
        String[] parts = contentType.split(";");
        for (String part : parts) {
            if (part.trim().startsWith("boundary=")) {
                return "--" + part.split("=")[1].trim();
            }
        }
        throw new IllegalArgumentException("Boundary not found in Content-Type");
    }

    private static String extractAttribute(String line, String attribute) {
        int start = line.indexOf(attribute + "=\"");
        if (start != -1) {
            int end = line.indexOf("\"", start + attribute.length() + 2);
            if (end != -1) {
                return line.substring(start + attribute.length() + 2, end);
            }
        }
        return null;
    }
}