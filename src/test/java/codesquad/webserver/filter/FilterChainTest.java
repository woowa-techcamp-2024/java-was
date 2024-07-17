package codesquad.webserver.filter;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.parser.RequestLine;
import codesquad.webserver.parser.enums.HttpMethod;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("FilterChain 테스트")
class FilterChainTest {

    @Test
    @DisplayName("필터를 추가하면 정렬된 순서로 저장되어야 한다")
    void addFilter_ShouldStoreFiltersInSortedOrder() {
        //Given
        FilterChain filterChain = new FilterChain();
        TestFilter filter1 = new TestFilter(1);
        TestFilter filter2 = new TestFilter(2);
        TestFilter filter3 = new TestFilter(4);
        //When
        filterChain.addFilter(filter1, 2);
        filterChain.addFilter(filter2, 1);
        filterChain.addFilter(filter3, 4);
        //Then
        assertThat(filterChain.getFiltersInOrder()).containsExactly(filter2, filter1, filter3);
    }

    @Test
    @DisplayName("모든 필터가 순서대로 실행되어야 한다")
    void doFilter_ShouldExecuteAllFiltersInOrder() {
        // Given
        FilterChain filterChain = new FilterChain();
        TestFilter filter1 = new TestFilter(1);
        TestFilter filter2 = new TestFilter(2);
        HttpRequest request = createTestHttpRequest("/test", "testSessionId");

        filterChain.addFilter(filter1, 1);
        filterChain.addFilter(filter2, 2);

        // When
        filterChain.doFilter(request);

        // Then
        assertThat(filter1.executed).isTrue();
        assertThat(filter2.executed).isTrue();
        assertThat(filter1.executionOrder).isLessThan(filter2.executionOrder);
    }

    @Test
    @DisplayName("필터에서 응답을 설정하면 필터 체인이 중단되어야 한다")
    void setResponse_ShouldStopFilterChain() {
        // Given
        FilterChain filterChain = new FilterChain();
        TestFilter filter1 = new TestFilter(1) {
            @Override
            public void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) {
                executed = true;
                executionOrder = executionCount++;
                HttpResponse newResponse = HttpResponse.builder()
                        .statusCode(302)
                        .header("Location", "/login")
                        .build();
                chain.setResponse(newResponse);
            }
        };
        TestFilter filter2 = new TestFilter(2);
        HttpRequest request = createTestHttpRequest("/test", "testSessionId");

        filterChain.addFilter(filter1, 1);
        filterChain.addFilter(filter2, 2);

        // When
        HttpResponse result = filterChain.doFilter(request);

        // Then
        assertThat(result.getStatusCode()).isEqualTo(302);
        assertThat(filter1.executed).isTrue();
        assertThat(filter2.executed).isFalse();
    }

//    @Test
//    @DisplayName("필터 실행 중 예외가 발생하면 서버 에러 응답을 반환해야 한다")
//    void doFilter_ShouldReturnServerErrorOnException() {
//        // Given
//        FilterChain filterChain = new FilterChain();
//        TestFilter filter = new TestFilter(1) {
//            @Override
//            public void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) {
//                throw new RuntimeException("Test Exception");
//            }
//        };
//        HttpRequest request = createTestHttpRequest("/test", "testSessionId");
//
//        filterChain.addFilter(filter, 1);
//
//        // When
//        HttpResponse response = filterChain.doFilter(request);
//
//        // Then
//        assertThat(response.getStatusCode()).isEqualTo(500);
//        assertThat(new String(response.getBody())).isEqualTo("Internal Server Error");
//    }

    private HttpRequest createTestHttpRequest(String path, String sessionId) {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Cookie", List.of("SID=" + sessionId));
        return new HttpRequest(
                new RequestLine(HttpMethod.GET, path, path, "HTTP/1.1"),
                headers,
                new HashMap<>(),
                ""
        );
    }

    private static class TestFilter implements Filter {
        boolean executed = false;
        int executionOrder = 0;
        static int executionCount = 0;
        private final int order;

        TestFilter(int order) {
            this.order = order;
        }

        @Override
        public void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) {
            executed = true;
            executionOrder = executionCount++;
        }

        @Override
        public int getOrder() {
            return order;
        }
    }
}