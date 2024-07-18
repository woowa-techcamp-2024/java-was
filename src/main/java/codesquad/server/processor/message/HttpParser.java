package codesquad.server.processor.message;

import codesquad.http.HttpRequest;
import codesquad.http.constant.HttpMethod;
import codesquad.http.constant.HttpVersion;
import codesquad.http.element.HttpHeader;
import codesquad.http.element.RequestBody;
import codesquad.http.element.HttpHeaders;
import codesquad.http.element.RequestStartLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.*;

import static codesquad.utils.StringUtil.CRLF;
import static codesquad.utils.StringUtil.SP;

public class HttpParser {
    private static final Logger logger = LoggerFactory.getLogger(HttpParser.class);

    public static HttpRequest parseHttpRequest(final byte[] inputMessage) throws Exception {
        List<byte[]> lines = parseLines(inputMessage);
        RequestStartLine requestStartLine = parseStartLine(new String(lines.get(0)));
        logger.debug(requestStartLine.toString());

        int emptyLineIndex = findEmptyLineIndex(lines);
        Map<String, HttpHeader> headers = parseHeaders(1, emptyLineIndex, lines);

        String body = null;
        if (emptyLineIndex != lines.size() - 1) body = parseBody(emptyLineIndex + 1, lines);
        if (headers.get("Content-Type") != null && headers.get("Content-Type").getFirstHeaderValue().equals("multipart/form-data")) {
            String boundary = headers.get("Content-Type").getHeaderValue("boundary");
            Map<String, String> map = parseFormData(emptyLineIndex + 1,lines, "--" + boundary + CRLF);
            return new HttpRequest(requestStartLine, new HttpHeaders(headers), new RequestBody(body, map));
        }
        return new HttpRequest(requestStartLine, new HttpHeaders(headers), new RequestBody(body));
    }

    private static List<byte[]> parseLines(byte[] inputMessage) {
        List<byte[]> list = new ArrayList<>();
        int first = 0;
        for (int i = 0; i < inputMessage.length; i++) {
            if (inputMessage[i] == CRLF.getBytes()[0] && inputMessage[i + 1] == CRLF.getBytes()[1]) {
                list.add(Arrays.copyOfRange(inputMessage, first, i + 2));
                first = i + 2;
            }
        }
        if (first != inputMessage.length) list.add(Arrays.copyOfRange(inputMessage, first, inputMessage.length));
        return list;
    }

    private static RequestStartLine parseStartLine(final String startLine) throws Exception {
        String[] words = startLine.split(SP);
        return new RequestStartLine(HttpMethod.getMethod(words[0]), new URI(words[1]), HttpVersion.HTTP_1_1);
    }

    private static int findEmptyLineIndex(List<byte[]> message) {
        for (int i = 1; i < message.size(); i++) {
            if ((new String(message.get(i)).equals(CRLF))) {
                return i;
            }
        }
        return message.size();
    }

    private static Map<String, HttpHeader> parseHeaders(int start, int end, List<byte[]> lines) {
        Map<String, HttpHeader> headers = new HashMap<>();
        for (int i = start; i < end; i++) {
            String message = new String(lines.get(i));
            if (message.equals(CRLF)) continue;

            String[] words = message.split(":");
            String[] values = words[1].split(";");
            parseHeader(words[0].trim(), values, headers);
        }
        return headers;
    }

    private static void parseHeader(final String message, final String[] values, final Map<String, HttpHeader> headers) {
        HttpHeader header = new HttpHeader();
        for (String value: values) header.append(value.trim().replace("\"", ""));
        headers.put(message, header);
    }

    private static String parseBody(int start, List<byte[]> lines) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = start; i < lines.size(); i++) {
            String message = new String(lines.get(i));
            stringBuilder.append(message).append(CRLF);
        }
        return stringBuilder.toString();
    }

    private static Map<String, String> parseFormData(int start, List<byte[]> lines, String boundary) throws IOException {
        Map<String, String> map = new HashMap<>();
        for (int i = start; i < lines.size(); i++) {
            String message = new String(lines.get(i));

            if (i != lines.size() - 1 && message.equals(boundary)) {
                int index = findFormDataHeaderIndex(i + 1, lines);
                Map<String, HttpHeader> headers = parseHeaders(i + 1, index, lines);
                HttpHeader header = headers.get("Content-Disposition");

                i = index + 1;
                ByteArrayOutputStream contentBuffer = new ByteArrayOutputStream();
                while (i < lines.size() && !new String(lines.get(i)).equals(boundary)) {
                    contentBuffer.write(lines.get(i));
                    i++;
                }
                byte[] content = contentBuffer.toByteArray();

                if (headers.containsKey("Content-Type")) {
                    map.put(header.getHeaderValue("name").replace("\"", ""), header.getHeaderValue("filename").replace("\"", ""));
                    uploadFile(header.getHeaderValue("filename"), content);
                    i--;
                }
                else {
                    map.put(header.getHeaderValue("name"), new String(content));
                    i--;
                }
            }
        }
        for (String key: map.keySet()) System.out.println(key + ": " + map.get(key));
        return map;
    }

    private static void uploadFile(String filename, byte[] content) {
        String uploadDir = System.getProperty("user.dir") + File.separator + "upload";
        File file = new File(uploadDir, filename);
        File parentDir = file.getParentFile();

        try {
            if (parentDir != null && !parentDir.exists()) {
                boolean dirCreated = parentDir.mkdirs();
                if (!dirCreated) {
                    throw new IOException();
                }
            }

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(content);
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.getStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    private static int findFormDataHeaderIndex(int start, List<byte[]> lines) {
        int i = start;
        while (i < lines.size() && !(new String(lines.get(i)).equals(CRLF))) i++;
        return i;
    }
}
