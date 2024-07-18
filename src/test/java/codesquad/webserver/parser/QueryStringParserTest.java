//package codesquad.webserver.parser;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import codesquad.webserver.httprequest.HttpRequest;
//import codesquad.webserver.parser.enums.HttpMethod;
//import java.util.Map;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//public class QueryStringParserTest {
//
//    private HttpRequest request;
//    private RequestLine requestLine;
//
//    @BeforeEach
//    public void setUp() {
//        request = new HttpRequest();
//        requestLine = new RequestLine(HttpMethod.GET, "path", "fullPath", "1.1");
//        request.setRequestLine(requestLine);
//    }
//
//    @Test
//    @DisplayName("정상적인 쿼리 스트링을 파싱해야 한다")
//    public void testParseValidQueryString() {
//        // Given
//        String path = "/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1";
//        request.getRequestLine().setFullPath(path);
//
//        // When
//        QueryStringParser.parseQueryString(request);
//        Map<String, String> params = request.getParams();
//
//        // Then
//        assertEquals(3, params.size(), "파싱된 쿼리 스트링의 파라미터 개수가 3개여야 합니다.");
//        assertEquals("javajigi", params.get("userId"));
//        assertEquals("password", params.get("password"));
//        assertEquals("박재성", params.get("name"));
//    }
//
//    @Test
//    @DisplayName("빈 쿼리 스트링을 파싱하면 빈 맵을 반환해야 한다")
//    public void testParseEmptyQueryString() {
//        // Given
//        String path = "/create";
//        request.getRequestLine().setPath(path);
//
//        // When
//        QueryStringParser.parseQueryString(request);
//        Map<String, String> params = request.getParams();
//
//        // Then
//        assertTrue(params.isEmpty(), "빈 쿼리 스트링은 빈 맵을 반환해야 합니다.");
//    }
//
//    @Test
//    @DisplayName("잘못된 형식의 쿼리 스트링을 무시해야 한다")
//    public void testParseInvalidQueryString() {
//        // Given
//        String path = "/create?userId=javajigi&invalid&password=password";
//        request.getRequestLine().setFullPath(path);
//
//        // When
//        QueryStringParser.parseQueryString(request);
//        Map<String, String> params = request.getParams();
//
//        // Then
//        assertEquals(2, params.size(), "유효한 쿼리 파라미터의 개수가 2개여야 합니다.");
//        assertEquals("javajigi", params.get("userId"));
//        assertEquals("password", params.get("password"));
//    }
//
//    @Test
//    @DisplayName("URL 인코딩된 값을 디코딩하여 파싱해야 한다")
//    public void testParseUrlEncodedValues() {
//        // Given
//        String path = "/create?name=%EB%B0%95%EC%9E%AC%EC%84%B1&city=%EC%84%9C%EC%9A%B8";
//        request.getRequestLine().setFullPath(path);
//
//        // When
//        QueryStringParser.parseQueryString(request);
//        Map<String, String> params = request.getParams();
//
//        // Then
//        assertEquals(2, params.size(), "파싱된 쿼리 스트링의 파라미터 개수가 2개여야 합니다.");
//        assertEquals("박재성", params.get("name"));
//        assertEquals("서울", params.get("city"));
//    }
//}
