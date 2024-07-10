package filter;

import http.HttpRequest;
import http.HttpResponse;
import java.io.IOException;

public class LoginFilter implements Filter{
    // 이곳에서 쿠키로 들어온 로그인 정보를 확인해준다.
    // 음... 여기는 일단 로그인 정보를 채취하는 곳


    @Override
    public void init() {

    }

    @Override
    public void doFilter(HttpRequest httpRequest, HttpResponse httpResponse,
        FilterChain filterChain) throws IOException {
        // 여기서 쿠키를 확인한다.
        String cookieStringValue = httpRequest.getHeader().getValue("Cookie");


        // 쿠키가 있다면 Session에서 유저 정보를 가져온다.
        filterChain.doFilter(httpRequest, httpResponse);
    }

    @Override
    public void destroy() {

    }
}
