package filter;


import http.HttpRequest;
import http.HttpResponse;
import java.io.IOException;
import java.sql.SQLException;

// 인터페이스로 변환
public interface Filter {
    void init();
    void doFilter(HttpRequest httpRequest, HttpResponse httpResponse, FilterChain filterChain)
        throws IOException, SQLException;
    void destroy();
}
