//package codesquad.webserver.parser;
//
//import codesquad.webserver.httprequest.HttpRequest;
//import java.util.Collections;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.StringReader;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//public class BodyParserTest {
//
//    private HttpRequest request;
//
//    @BeforeEach
//    public void setUp() {
//        request = new HttpRequest();
//    }
//
//
//    @Test
//    @DisplayName("Content-Length 헤더를 사용하여 요청 본문을 성공적으로 파싱해야 한다")
//    public void testParseBodyWithContentLength() throws IOException {
//        // Given
//        String bodyContent = "{\"name\":\"John Doe\",\"age\":30}";
//        Map<String, List<String>> headers = new HashMap<>();
//        headers.put("Content-Type", Collections.singletonList("application/json"));
//        headers.put("Content-Length",Collections.singletonList(String.valueOf(bodyContent.length())));
//        BufferedReader in = new BufferedReader(new StringReader(bodyContent));
//
//        request.setHeaders(headers);
//        request.setBody(bodyContent);
//        // When
//        BodyParser.parse(in, request);
//
//        // Then
//        assertEquals(bodyContent, request.getBody(), "요청 본문이 정확히 파싱되어야 합니다.");
//    }
//
//    @Test
//    @DisplayName("Content-Length 헤더가 없으면 빈 문자열을 반환해야 한다")
//    public void testParseBodyWithoutContentLength() throws IOException {
//        // Given
//        String bodyContent = "{\"name\":\"John Doe\",\"age\":30}";
//        Map<String, List<String>> headers = new HashMap<>();
//        headers.put("Content-Type", Collections.singletonList("application/json"));
//        BufferedReader in = new BufferedReader(new StringReader(bodyContent));
//
//        request.setBody(bodyContent);
//        request.setHeaders(headers);
//        // When
//        BodyParser.parse(in, request);
//
//        // Then
//        assertEquals("", request.getBody(), "Content-Length 헤더가 없으면 빈 문자열을 반환해야 합니다.");
//    }
//
//    @Test
//    @DisplayName("Content-Length 헤더 값이 잘못된 경우 IOException을 발생시켜야 한다")
//    public void testParseBodyWithInvalidContentLength() {
//        // Given
//        String bodyContent = "{\"name\":\"John Doe\",\"age\":30}";
//        Map<String, List<String>> headers = new HashMap<>();
//        headers.put("Content-Type", Collections.singletonList("application/json"));
//        headers.put("Content-Length", Collections.singletonList("invalid"));
//        BufferedReader in = new BufferedReader(new StringReader(bodyContent));
//
//        request.setBody(bodyContent);
//        request.setHeaders(headers);
//
//        // When & Then
//        IOException exception = assertThrows(IOException.class, () -> {
//            BodyParser.parse(in, request);
//        });
//        assertEquals("Invalid Content-Length value: invalid", exception.getMessage(), "잘못된 Content-Length 헤더 값이 있으면 IOException이 발생해야 합니다.");
//    }
//}
