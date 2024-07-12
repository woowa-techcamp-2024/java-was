package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Header {

    private final Map<String, String> header;
    private final Map<String, Cookie> cookieMap = new HashMap<>();


    private Header(String headerString) {
        this.header = headerMapper(headerString);
    }

    private Header() {
        this.header = new HashMap<>();
    }

    public static Header emptyHeader() {
        return new Header();
    }

    public static Header from(BufferedReader bufferedReader) throws IOException {
        StringBuilder headerString = new StringBuilder();
        do {
            String line = bufferedReader.readLine();
            if (line == null || line.isEmpty()) {
                break;
            }
            headerString.append(line).append("\r\n");
        } while (true);

        return new Header(headerString.toString());
    }

    private Map<String, String> headerMapper(String headerString) {
        if (headerString == null) {
            throw new IllegalArgumentException("헤더 문자열은 null일 수 없습니다.");
        }

        Map<String, String> headerMap = new HashMap<>();
        String[] splitHeaderLines = splitHeaderLine(headerString);
        for (String splitHeaderLine : splitHeaderLines) {
            processOneHeaderLine(headerMap, splitHeaderLine);
        }

        return headerMap;
    }

    private String[] splitHeaderLine(String headerString) {
        return headerString.split("\r\n");
    }

    private void processOneHeaderLine(Map<String, String> headerMap, String headerLine) {
        if (!headerLine.trim().isEmpty()) {
            String[] splitHeaderLine = splitOneHeaderLine(headerLine);
            String key = splitHeaderLine[0];
            String value = splitHeaderLine.length > 1 ? splitHeaderLine[1] : "";

            if (headerMap.containsKey(key)) {
                headerMap.put(key, headerMap.get(key) + ", " + value);
            } else {
                headerMap.put(key, value);
            }
        }
    }

    private String[] splitOneHeaderLine(String headerString) {
        if (headerString == null) {
            throw new IllegalArgumentException("null 값은 들어올 수 없습니다.");
        }

        String[] headerParts = headerString.split(":", 2);
        if (headerParts.length != 2) {
            throw new IllegalArgumentException("유효하지 않은 헤더 형식입니다: " + headerString);
        }

        String key = headerParts[0].trim();
        String value = headerParts[1].trim();

        if (key.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 헤더 형식입니다: " + headerString);
        }

        return new String[]{key, value};
    }

    public String getValue(String key) {
        return header.getOrDefault(key, "");
    }

    public void addKey(String key, String value) {
        header.put(key, value);
    }

    public void addCookie(Cookie cookie) {
        cookieMap.put(cookie.getName(), cookie);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : header.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }

        for (Map.Entry<String, Cookie> entry : cookieMap.entrySet()) {
            sb.append(entry.getValue().toString()).append("\r\n");
        }

        return sb.toString();
    }
}
