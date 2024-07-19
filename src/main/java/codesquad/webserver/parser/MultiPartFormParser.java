package codesquad.webserver.parser;

import codesquad.webserver.http.MultiPartFormData;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiPartFormParser {

    private static final Logger logger = LoggerFactory.getLogger(MultiPartFormParser.class);

    public static Map<String, MultiPartFormData> parse(InputStream inputStream, String boundary) throws IOException {
        Map<String, MultiPartFormData> result = new HashMap<>();

        byte[] boundaryLine = ("--" + boundary).getBytes("UTF-8");
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] requestBody = outputStream.toByteArray();

        int readIdx = 0;
        while (readIdx < requestBody.length) {
            int boundaryStartIdx = indexOf(requestBody, boundaryLine, readIdx);
            if (boundaryStartIdx == -1) {
                break;
            }
            int partStartIdx = boundaryStartIdx + boundaryLine.length + 2;  // CRLF 2byte
            int partEndIdx = indexOf(requestBody, boundaryLine, partStartIdx);  // CRLF 2byte
            if (partEndIdx == -1) {
                break;
            }
            byte[] part = Arrays.copyOfRange(requestBody, partStartIdx, partEndIdx - 2);
            MultiPartFormData multiPartFormData = mapFrom(part);
            result.put(multiPartFormData.getName(), multiPartFormData);
            readIdx = partEndIdx;   // CRLF 2byte
        }
        return result;
    }

    private static MultiPartFormData mapFrom(byte[] part) throws IOException {
        // find header
        int headerStartIdx = 0;
        int headerEndIdx = indexOf(part, "\r\n\r\n".getBytes("UTF-8"), 0);
        if (headerEndIdx == -1) {
            throw new IOException("Invalid part data: missing headers");
        }
        byte[] header = Arrays.copyOfRange(part, headerStartIdx, headerEndIdx);

        // find content
        int contentStartIdx = headerEndIdx + 4;    // headerCRLF + CRLF
        int contentEndIdx = part.length;
        byte[] content = Arrays.copyOfRange(part, contentStartIdx, contentEndIdx);

        BufferedReader headerReader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(header), "UTF-8"));

        String line;
        String name = "";
        String fileName = "";
        String contentType = "";
        while ((line = headerReader.readLine()) != null && !line.isBlank()) {
            logger.debug("2.....");
            if (line.startsWith("Content-Disposition")) {
                name = extractValueOfName(line, "name");
                fileName = extractValueOfName(line, "filename");

            }
            if (line.startsWith("Content-Type")) {
                contentType = extractContentType(line);
            }
        }
        return new MultiPartFormData(name, fileName, contentType, content);
    }

    private static String extractValueOfName(String line, String name) {
        String target = line.split("; ")[1];
        return target.split("=")[1].replace("\"", "");
    }

    private static String extractContentType(String line) {
        return line.split(": ")[1];
    }

    private static int indexOf(byte[] requestBody, byte[] target, int start) {
        for (int startIdx = start; startIdx <= requestBody.length - target.length; startIdx++) {
            boolean match = true;
            for (int compareIdx = 0; compareIdx < target.length; compareIdx++) {
                if (requestBody[startIdx + compareIdx] != target[compareIdx]) {
                    match = false;
                    break;
                }
            }
            if (match) {
                return startIdx;
            }
        }
        return -1;
    }
}
