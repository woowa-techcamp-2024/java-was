package codesquad;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HttpRequestMessageTest {

    @Nested
    @DisplayName("parse 호출 시")
    class ParseTest {

        @Test
        @DisplayName("입력으로 주어진 HTTP GET 메시지를 파싱한다.")
        void parseInputMessage() {
            //given
            String rawHttpMessage = """
                    GET /index.html HTTP/1.1
                    Host: localhost:8080
                    Connection: keep-alive
                    Cache-Control: max-age=0""";

            //when
            HttpRequestMessage httpRequestMessage = HttpRequestMessage.parse(rawHttpMessage);

            //then
            assertEquals("GET", httpRequestMessage.method());
            assertEquals("/index.html", httpRequestMessage.requestUrl());
            assertEquals("HTTP/1.1", httpRequestMessage.httpVersion());
            assertEquals("localhost:8080", httpRequestMessage.header().get("Host"));
            assertEquals("keep-alive", httpRequestMessage.header().get("Connection"));
            assertEquals("max-age=0", httpRequestMessage.header().get("Cache-Control"));
        }
    }
}