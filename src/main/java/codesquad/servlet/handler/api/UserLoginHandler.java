package codesquad.servlet.handler.api;

import codesquad.domain.InMemoryUserStorage;
import codesquad.domain.model.User;
import codesquad.servlet.SessionStorage;
import codesquad.servlet.handler.Handler;
import codesquad.webserver.http.Cookie;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserLoginHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationHandler.class);
    private final InMemoryUserStorage inMemoryUserStorage;
    private final SessionStorage sessionStorage;

    public UserLoginHandler(InMemoryUserStorage inMemoryUserStorage, SessionStorage sessionStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.sessionStorage = sessionStorage;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {

        String userId = request.getBodyParamValue("userId");
        String password = request.getBodyParamValue("password");

        logger.debug("userId = {}, password = {}", userId, password);

        User findUser = inMemoryUserStorage.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (!findUser.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String createdSessionId = sessionStorage.createSession(findUser);
        logger.debug("created session id: {}", createdSessionId);

        Cookie cookie = new Cookie("sid", createdSessionId);
        cookie.setPath("/");
        response.addCookie(cookie);
        logger.debug("crated cookie: {}", cookie);
        response.sendRedirect("/main");
    }

}
