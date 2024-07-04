package http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import http.startline.RequestLine;
import http.startline.StartLine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class HttpTest {

    private static Stream<Arguments> provideValidHttpRequests() {
        return Stream.of(
            Arguments.of(
                "GET /index.html HTTP/1.1\r\nHost: www.example.com\r\nConnection: keep-alive\r\nContent-Length: 12\r\n\r\nbody content",
                "GET", "/index.html", "HTTP/1.1",
                "Host: www.example.com\r\nConnection: keep-alive\r\nContent-Length: 12",
                "body content"
            ),
            Arguments.of(
                "GET /index.html HTTP/1.1\r\nHost: www.example.com\r\nConnection: keep-alive\r\nContent-Length: 0\r\n\r\n",
                "GET", "/index.html", "HTTP/1.1",
                "Host: www.example.com\r\nConnection: keep-alive\r\nContent-Length: 0", ""
            ),
            Arguments.of(
                "GET /index.html HTTP/1.1\r\nContent-Length: 12\r\n\r\nbody content",
                "GET", "/index.html", "HTTP/1.1", "Content-Length: 12", "body content"
            )
        );
    }

    private static Stream<Arguments> provideInvalidHttpRequests() {
        return Stream.of(
            Arguments.of(" ", "Request Line 형식이 잘못되었습니다."),
            Arguments.of("", "HTTP 메시지가 비어 있습니다."),
            Arguments.of("\r\nHost: www.example.com\r\nConnection: keep-alive\r\n\r\nbody content",
                "Request Line 형식이 잘못되었습니다.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidHttpRequests")
    @DisplayName("유효한 HTTP 요청 테스트")
    public void testGenerate(String httpRequest, String expectedMethod, String expectedPath,
        String expectedVersion, String expectedHeaders, String expectedBody) throws IOException {
        Http http = Http.generate(new BufferedReader(new StringReader(httpRequest)));
        StartLine startLine = http.getStartLine();
        Header header = http.getHeader();
        String body = http.getBody();

        Map<String, String> headerMap1 = parseHeaders(expectedHeaders);
        Map<String, String> headerMap2 = parseHeaders(header.toString());

        RequestLine requestLine = (RequestLine) startLine;
        assertEquals(expectedMethod, requestLine.getMethod().name());
        assertEquals(expectedPath, requestLine.getUrlPath().getPath());
        assertEquals(expectedVersion, requestLine.getVersion());
        assertEquals(headerMap1, headerMap2);
        assertEquals(expectedBody, body);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidHttpRequests")
    @DisplayName("유효하지 않은 HTTP 요청 테스트")
    public void testGenerate(String httpRequest, String expectedErrorMessage) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Http.generate(new BufferedReader(new StringReader(httpRequest)));
        });
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    public static Map<String, String> parseHeaders(String headers) {
        Map<String, String> headerMap = new HashMap<>();
        String[] lines = headers.split("\r\n");
        for (String line : lines) {
            String[] parts = line.split(": ", 2);
            if (parts.length == 2) {
                headerMap.put(parts[0], parts[1]);
            }
        }
        return headerMap;
    }
}
