package codesquad.was.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class RequestInputStreamReaderTest {

    @Test
    void HTTP_Request를_바이트기반으로_파싱할_수_있다() throws IOException {
        // given
        String httpRequestValue =
                "POST /submit-form HTTP/1.1\r\n"
                        + "Host: localhost\r\n"
                        + "Connection: keep-alive\r\n"
                        + "Content-Type: application/x-www-form-urlencoded\r\n"
                        + "Content-Length: " + "name=JohnDoe".getBytes().length + "\r\n"
                        + "\r\n"
                        + "name=JohnDoe";

        // when
        RequestInputStreamReader reader = new RequestInputStreamReader(
                new BufferedInputStream(new ByteArrayInputStream(httpRequestValue.getBytes())));

        // then
        assertAll(
                () -> assertThat(reader.readRequestLine()).isEqualTo("POST /submit-form HTTP/1.1"),
                () -> assertThat(reader.readHeaders()).containsExactly("Host: localhost",
                        "Connection: keep-alive",
                        "Content-Type: application/x-www-form-urlencoded",
                        "Content-Length: " + "name=JohnDoe".getBytes().length),
                () -> assertThat(reader.readBody("name=JohnDoe".getBytes().length)).containsExactly(
                        "name=JohnDoe".getBytes())
        );
    }
}
