package codesquad.servlet.handler.api;

import codesquad.domain.model.User;
import codesquad.servlet.SessionStorage;
import codesquad.servlet.handler.Handler;
import codesquad.webserver.http.Cookie;
import codesquad.webserver.http.HttpMediaType;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserInfoHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoHandler.class);
    private final SessionStorage sessionStorage;

    public UserInfoHandler(SessionStorage sessionStorage) {
        this.sessionStorage = sessionStorage;
    }

    public void service(HttpRequest request, HttpResponse response) {
        logger.debug("UserInfoHandler start");
        logger.debug("all session = {}", sessionStorage.getSessions());
        // 사용자 인증
        Cookie cookie = request.getCookie();
        // TODO: 쿠키값과 세션값 검증 로직
        String uuid = cookie.getValue();
        // TODO: 세션에서 가져오면 X (DB 동기화 이슈)
        // TODO: HTTPRequest/HTTPResponse Builder 패턴 추천
        User user = sessionStorage.findByUuid(uuid).orElseThrow(() -> new IllegalArgumentException("세션에 존재하지 않습니다."));
        UserInfo userInfo = new UserInfo(user);
        logger.debug("UserInfoHandler end");
        response.setContentType(HttpMediaType.APPLICATION_JSON);
        response.setBody(userInfo.toString().getBytes());
    }

}
