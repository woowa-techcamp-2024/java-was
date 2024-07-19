package codesquad.webserver.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import codesquad.webserver.httprequest.HttpRequest;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BodyParserTest {

    private HttpRequest request;

    @BeforeEach
    public void setUp() {
        request = new HttpRequest();
    }

    @Test
    @DisplayName("Content-Length 헤더를 사용하여 요청 본문을 성공적으로 파싱해야 한다")
    public void testParseBodyWithContentLength() throws IOException {
        // Given
        String bodyContent = "{\"name\":\"John Doe\",\"age\":30}";
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", Collections.singletonList("application/json"));
        headers.put("Content-Length", Collections.singletonList(String.valueOf(bodyContent.length())));
        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(bodyContent.getBytes()));

        request.setHeaders(headers);

        // When
        BodyParser.parse(in, request);

        // Then
        assertEquals(bodyContent, request.getBody(), "요청 본문이 정확히 파싱되어야 합니다.");
    }

    @Test
    @DisplayName("Content-Length 헤더가 없으면 빈 문자열을 반환해야 한다")
    public void testParseBodyWithoutContentLength() throws IOException {
        // Given
        String bodyContent = "{\"name\":\"John Doe\",\"age\":30}";
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", Collections.singletonList("application/json"));
        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(bodyContent.getBytes()));

        request.setHeaders(headers);

        // When
        BodyParser.parse(in, request);

        // Then
        assertEquals("", request.getBody(), "Content-Length 헤더가 없으면 빈 문자열을 반환해야 합니다.");
    }
}
