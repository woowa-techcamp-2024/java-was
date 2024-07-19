package util;

import http.Header;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class QueryParserUtil {
    private final static Logger logger = LoggerUtil.getLogger();

    public static Map<String, String> parseQuery(String queryString) {
        if (queryString == null || queryString.isEmpty()) {
            return new HashMap<>();
        }

        return Arrays.stream(queryString.split("&"))
            .map(pair -> pair.split("=", 2))
            .collect(Collectors.toMap(
                keyValue -> URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8),
                keyValue -> keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8) : "",
                (value1, value2) -> value1 // In case of duplicate keys, keep the first one
            ));
    }

    public static Map<String, Map<String, byte[]>> parseMulti(byte[] body, String boundary) {
        Map<String, Map<String, byte[]>> result = new HashMap<>();
        String delimiter = "--" + boundary;
        byte[] delimiterBytes = delimiter.getBytes();
        byte[] newLineBytes = "\r\n".getBytes();
        byte[] endDelimiterBytes = (delimiter + "--").getBytes();

        int startIndex = 0;

        while (startIndex < body.length) {
            int boundaryIndex = indexOf(body, delimiterBytes, startIndex);
            if (boundaryIndex == -1) break;

            startIndex = boundaryIndex + delimiterBytes.length;
            if (startIndex >= body.length) break;

            if (indexOf(body, endDelimiterBytes, boundaryIndex) == boundaryIndex) {
                break; // 마지막 구분자 처리
            }

            startIndex += newLineBytes.length; // Skip \r\n

            int headerEndIndex = indexOf(body, "\r\n\r\n".getBytes(), startIndex);
            if (headerEndIndex == -1) continue;

            String headers = new String(body, startIndex, headerEndIndex - startIndex);
            int contentStartIndex = headerEndIndex + 4;
            int contentEndIndex = indexOf(body, delimiterBytes, contentStartIndex) - 2;

            if (contentEndIndex < contentStartIndex) continue; // End of part not found correctly

            byte[] content = Arrays.copyOfRange(body, contentStartIndex, contentEndIndex);

            Map<String, String> headersMap = Header.headerMapper(headers);
            String contentDisposition = headersMap.get("Content-Disposition");
            if (contentDisposition != null) {
                String name = extractNameFromContentDisposition(contentDisposition);
                if (name != null) {
                    Map<String, byte[]> partMap = new HashMap<>();
                    for (Map.Entry<String, String> entry : headersMap.entrySet()) {
                        partMap.put(entry.getKey(), entry.getValue().getBytes());
                    }
                    partMap.put("content", content);
                    result.put(name, partMap);
                }
            }

            startIndex = contentEndIndex + newLineBytes.length;
        }
        return result;
    }

    private static int indexOf(byte[] outerArray, byte[] smallerArray, int start) {
        for (int i = start; i < outerArray.length - smallerArray.length + 1; i++) {
            boolean found = true;
            for (int j = 0; j < smallerArray.length; j++) {
                if (outerArray[i + j] != smallerArray[j]) {
                    found = false;
                    break;
                }
            }
            if (found) return i;
        }
        return -1;
    }

    private static String extractNameFromContentDisposition(String contentDisposition) {
        Pattern pattern = Pattern.compile("name=\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(contentDisposition);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getBoundaryFromContentType(String contentType) {
        if (contentType == null) return null;
        Pattern pattern = Pattern.compile("boundary=(.*)");
        Matcher matcher = pattern.matcher(contentType);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
