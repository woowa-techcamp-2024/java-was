package codesquad.webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestParser {
    private static final Logger log = LoggerFactory.getLogger(HttpRequestParser.class);

    public static HttpRequest parse(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        HttpRequestStartLine startLine = new HttpRequestStartLine(readStartLine(br));
        HttpHeader headers = readHeader(br);
        return new HttpRequest(startLine, headers);
    }

    private static String readStartLine(BufferedReader br) {
        try {
            String line = br.readLine();
            if (line == null) {
                log.error("");
            }
            return line;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpHeader readHeader(BufferedReader br) {
        try{
            HttpHeader headers = new HttpHeader();
            String line;
            // \r\n 까지 읽는다
            while(!(line = br.readLine()).isEmpty()){
                String[] row = line.split(":");
                headers.add(row[0], row[1].trim());
            }
            return headers;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
