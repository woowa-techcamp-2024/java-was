package http.startline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class UrlPathTest {

    @Test
    @DisplayName("경로만 있는 URL 경로 테스트")
    public void testPathOnly() {
        UrlPath urlPath = UrlPath.of("/path/to/resource");
        assertEquals("/path/to/resource", urlPath.getPath());
        assertEquals(0, urlPath.getQueryParameters().size());
    }

    @Test
    @DisplayName("쿼리 파라미터가 포함된 URL 경로 테스트")
    public void testPathWithQuery() {
        UrlPath urlPath = UrlPath.of("/path/to/resource?param1=value1&param2=value2");
        assertEquals("/path/to/resource", urlPath.getPath());
        Map<String, String> params = urlPath.getQueryParameters();
        assertEquals(2, params.size());
        assertEquals("value1", params.get("param1"));
        assertEquals("value2", params.get("param2"));
    }

    @Test
    @DisplayName("쿼리 파라미터가 하나만 있는 URL 경로 테스트")
    public void testPathWithSingleQueryParameter() {
        UrlPath urlPath = UrlPath.of("/path/to/resource?param1=value1");
        assertEquals("/path/to/resource", urlPath.getPath());
        Map<String, String> params = urlPath.getQueryParameters();
        assertEquals(1, params.size());
        assertEquals("value1", params.get("param1"));
    }

    @Test
    @DisplayName("쿼리 파라미터가 빈 값인 URL 경로 테스트")
    public void testPathWithEmptyQueryParameter() {
        UrlPath urlPath = UrlPath.of("/path/to/resource?param1=");
        assertEquals("/path/to/resource", urlPath.getPath());
        Map<String, String> params = urlPath.getQueryParameters();
        assertEquals(1, params.size());
        assertEquals("", params.get("param1"));
    }

    @Test
    @DisplayName("값이 없는 쿼리 파라미터가 포함된 URL 경로 테스트")
    public void testPathWithNoValueQueryParameter() {
        UrlPath urlPath = UrlPath.of("/path/to/resource?param1");
        assertEquals("/path/to/resource", urlPath.getPath());
        Map<String, String> params = urlPath.getQueryParameters();
        assertEquals(1, params.size());
        assertEquals("", params.get("param1"));
    }

    @Test
    @DisplayName("여러 개의 쿼리 파라미터가 포함된 URL 경로 테스트")
    public void testMultipleQueryParametersWithSomeEmptyValues() {
        UrlPath urlPath = UrlPath.of("/path/to/resource?param1=value1&param2=&param3=value3");
        assertEquals("/path/to/resource", urlPath.getPath());
        Map<String, String> params = urlPath.getQueryParameters();
        assertEquals(3, params.size());
        assertEquals("value1", params.get("param1"));
        assertEquals("", params.get("param2"));
        assertEquals("value3", params.get("param3"));
    }

    @Test
    @DisplayName("존재하지 않는 쿼리 파라미터를 가져오려고 할 때 테스트")
    public void testGetQueryParameter() {
        UrlPath urlPath = UrlPath.of("/path/to/resource?param1=value1&param2=value2");
        assertEquals("value1", urlPath.getQueryParameter("param1"));
        assertEquals("value2", urlPath.getQueryParameter("param2"));
        assertNull(urlPath.getQueryParameter("param3"));
    }

    @Test
    @DisplayName("쿼리 파라미터에 인코딩된 문자가 포함된 URL 경로 테스트")
    public void testEncodedCharactersInQuery() {
        UrlPath urlPath = UrlPath.of("/path/to/resource?param1=value%201&param2=value%202");
        assertEquals("/path/to/resource", urlPath.getPath());
        Map<String, String> params = urlPath.getQueryParameters();
        assertEquals(2, params.size());
        assertEquals("value 1", params.get("param1"));
        assertEquals("value 2", params.get("param2"));
    }

    @Test
    @Disabled
    @DisplayName("프래그먼트가 포함된 URL 경로 테스트")
    public void testPathWithFragment() {
        UrlPath urlPath = UrlPath.of("/path/to/resource?param1=value1#fragment");
        assertEquals("/path/to/resource", urlPath.getPath());
        Map<String, String> params = urlPath.getQueryParameters();
        assertEquals(1, params.size());
        assertEquals("value1", params.get("param1"));
    }

    @Test
    @DisplayName("빈 경로 테스트")
    public void testEmptyPath() {
        UrlPath urlPath = UrlPath.of("");
        assertEquals("", urlPath.getPath());
        assertEquals(0, urlPath.getQueryParameters().size());
    }

    @Test
    @DisplayName("루트 경로 테스트")
    public void testRootPath() {
        UrlPath urlPath = UrlPath.of("/");
        assertEquals("/", urlPath.getPath());
        assertEquals(0, urlPath.getQueryParameters().size());
    }

    @Test
    @DisplayName("쿼리 파라미터만 있는 경우 테스트")
    public void testOnlyQueryParameters() {
        UrlPath urlPath = UrlPath.of("?param1=value1&param2=value2");
        assertEquals("", urlPath.getPath());
        Map<String, String> params = urlPath.getQueryParameters();
        assertEquals(2, params.size());
        assertEquals("value1", params.get("param1"));
        assertEquals("value2", params.get("param2"));
    }
}
