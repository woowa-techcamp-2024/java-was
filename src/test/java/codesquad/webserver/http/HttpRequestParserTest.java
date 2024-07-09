package codesquad.webserver.http;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpRequestParserTest {
    @Test
    public void parse(){
        String rawRequest =
                "POST /index?name=value HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Accept: text/html,application/xhtml+xml\r\n" +
                        "Content-Length: 28\r\n" +
                        "\r\n" +
                        "\"message\"=\"Hello, World!\"";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.UTF_8));
        HttpRequest request = HttpRequestParser.parse(inputStream);
        // start line
        assertEquals(HttpMethod.POST, request.getHttpMethod());
        assertEquals("/index", request.getPath());
        assertEquals("name=value", request.getQueryString());
        // header
        HttpHeader header = request.getHttpHeader();
        assertTrue(header.getKeys().contains("Host"));
        assertTrue(header.getKeys().contains("Accept"));
        assertEquals("localhost:8080", header.getValue("Host"));
        // body
        //assertEquals("Hello, World!", request.getHttpBody().get("message"));
    }
}
