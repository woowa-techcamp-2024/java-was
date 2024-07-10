package codesquad.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

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

            BufferedReader reader = new BufferedReader(new StringReader(httpRequestText));
            String result = httpRequestReader.read(reader);

            assertThat(result).isEqualTo(expected);
        }
    }

    @Nested
    class readBody_메서드는 {

        String requestBodyText = "name=Javajigi&password=password&email=javajigi%40slipp.net";
        int contentLength = requestBodyText.getBytes().length;

        @Test
        void request_body를_읽을_수_있다() throws IOException {
            BufferedReader reader = new BufferedReader(new StringReader(requestBodyText));
            String result = httpRequestReader.readBody(reader, contentLength);

            assertThat(result).isEqualTo(requestBodyText);
        }
    }

}