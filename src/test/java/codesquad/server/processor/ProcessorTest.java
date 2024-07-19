package codesquad.server.processor;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.constant.HttpMethod;
import codesquad.http.constant.HttpStatus;
import codesquad.http.constant.HttpVersion;
import codesquad.http.element.HttpHeaders;
import codesquad.http.element.ResponseStartLine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ProcessorTest {
    Processor processor = new Processor();

    @Test
    @DisplayName("메세지에 맞는 http 요청을 반환해야 합니다")
    void processRequestTest() throws Exception {
        String httpMessage = "GET /test HTTP/1.1\r\nHost: localhost\r\n\r\n";
        HttpRequest httpRequest = processor.processRequest(httpMessage.getBytes());

        assertEquals(HttpMethod.GET, httpRequest.getRequestStartLine().method());
        assertEquals(new URI("/test"), httpRequest.getRequestStartLine().uri());
        assertEquals(HttpVersion.HTTP_1_1, httpRequest.getRequestStartLine().version());
    }

    @Test
    @DisplayName("응답에 맞는 byte[]를 반환해야 합니다")
    void processResponseTest() throws IOException {
        ResponseStartLine responseStartLine = new ResponseStartLine(HttpVersion.HTTP_1_1, HttpStatus.OK);
        HttpResponse httpResponse = new HttpResponse(responseStartLine, new HttpHeaders(new HashMap<>()), null);
        httpResponse.getHttpHeaders().appendHeader("Content-Type", "text/html");

        String expectedResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n";
        byte[] expectedOutputMessage = expectedResponse.getBytes(StandardCharsets.UTF_8);
        byte[] outputMessage = processor.processResponse(httpResponse);
        assertArrayEquals(expectedOutputMessage, outputMessage);
    }
}