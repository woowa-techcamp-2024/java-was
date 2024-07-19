package codesquad.webserver.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HttpResponseParserTest {

    private HttpResponse response;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    void testParseWithSimpleResponse() throws IOException {
        HttpResponse response = HttpResponse.ok("Hello, World!");
        HttpResponseParser.parse(outputStream, response);

        String result = outputStream.toString("UTF-8");
        String expected =
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Length: 13\r\n" +
                        "Content-Type: text/html;charset=utf-8\r\n" +
                        "\r\n" +
                        "Hello, World!";

        assertEquals(expected, result);
    }
}
