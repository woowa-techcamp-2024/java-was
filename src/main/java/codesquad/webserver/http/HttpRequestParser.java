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
        HttpHeader header = readHeader(br);
        HttpBody body = null;
        if(header.hasKey("Content-Type") && header.hasKey("Content-Length")){
            body = readBody(br, header.getValue("Content-Type"), Integer.parseInt(header.getValue("Content-Length")));
        }
        return new HttpRequest(startLine, header, body);
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
                String[] row = line.split(":\\s*", 2);
                headers.add(row[0], row[1]);
                log.debug("header line: {}", line);
            }
            return headers;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpBody readBody(BufferedReader br, String contentType, int contentLength){
        try{
            char[] buffer = new char[contentLength];
            br.read(buffer, 0 , contentLength);
            String mesage = new String(buffer);
            if(contentType.equals("application/json")){
                return new HttpBody(HttpRequestBodyParser.parseJson(mesage));
            }
            else if(contentType.equals("application/x-www-form-urlencoded")){
                return new HttpBody(HttpRequestBodyParser.parseForm(mesage));
            }
            else {
                throw new IOException("");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
