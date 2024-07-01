package codesquad.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

public class HttpParserTest {


    // Parses a valid HTTP GET request correctly
    @Test
    public void test_parses_valid_http_get_request_correctly() throws IOException {
        // Given
        String httpRequest = "GET /index.html HTTP/1.1\nHost: example.com\n\n";
        BufferedReader in = new BufferedReader(new StringReader(httpRequest));
        HttpParser parser = new HttpParser();

        // When
        HttpRequest request = parser.parse(in);

        // Then
        assertEquals("GET", request.method());
        assertEquals("/index.html", request.path());
        assertEquals("HTTP/1.1", request.httpVersion());
        assertEquals(" example.com", request.headers().get("Host"));
    }

    // Parses a valid HTTP POST request correctly
    @Test
    public void test_parses_valid_http_post_request_correctly() throws IOException {
        // Given
        String httpRequest = "POST /submit HTTP/1.1\nHost: example.com\nContent-Length: 27\n\n";
        BufferedReader in = new BufferedReader(new StringReader(httpRequest));
        HttpParser parser = new HttpParser();

        // When
        HttpRequest request = parser.parse(in);

        // Then
        assertEquals("POST", request.method());
        assertEquals("/submit", request.path());
        assertEquals("HTTP/1.1", request.httpVersion());
        assertEquals(" example.com", request.headers().get("Host"));
        assertEquals(" 27", request.headers().get("Content-Length"));
    }

    // Throws IOException when request line is null
    @Test
    public void test_throws_ioexception_when_request_line_is_null() {
        // Given
        BufferedReader in = new BufferedReader(new StringReader(""));
        HttpParser parser = new HttpParser();

        // When & Then
        assertThrows(IOException.class, () -> {
            parser.parse(in);
        });
    }

    // Throws IOException when request line has less than three parts
    @Test
    public void test_throws_ioexception_when_request_line_has_less_than_three_parts() {
        // Given
        String httpRequest = "GET /index.html\n";
        BufferedReader in = new BufferedReader(new StringReader(httpRequest));
        HttpParser parser = new HttpParser();

        // When & Then
        assertThrows(IOException.class, () -> {
            parser.parse(in);
        });
    }

}
