package codesquad.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

class HttpRequestReaderTest {

    HttpRequestReader httpRequestReader;

    @BeforeEach
    void setUp() {
        httpRequestReader = HttpRequestReader.getInstance();
    }

    @Nested
    class read_메서드는 {

        String httpRequestText = "GET / HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "\r\n";

        @Test
        void request_header를_읽을_수_있다() throws IOException {
            String expected = "GET / HTTP/1.1\r\n" +
                    "Host: localhost\r\n";
            httpRequestReader.readHeader(new ByteArrayInputStream(httpRequestText.getBytes()));
        }
    }

    @Nested
    class readBody_메서드는 {

        String requestBodyText = "name=Javajigi&password=password&email=javajigi%40slipp.net";
        int contentLength = requestBodyText.getBytes().length;

        @Test
        void request_body를_읽을_수_있다() throws IOException {
            // TODO test
        }
    }

}