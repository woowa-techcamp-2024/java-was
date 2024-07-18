package handler;

import http.Cookie;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import repository.UserRepository;
import service.UserService;
import session.Session;
import session.SessionManager;
import util.LoggerUtil;
import util.StaticPage;

public class LoginHandler extends MyHandler{
    private static final Logger logger = LoggerUtil.getLogger();
    private final UserService userService = new UserService(UserRepository.getInstance());

    @Override
    void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        logger.info("LoginHandler doPost");
        Map<String, String> bodyParams = httpRequest.bodyParamsString();
        String userId = bodyParams.get("username");
        String password = bodyParams.get("password");
        logger.info("userId: {}, password: {}", userId, password);
        User user = userService.findUserById(userId);
        if (user == null || !user.getPassword().equals(password)) {
            logger.info("Login fail");
            ResponseValueSetter.redirect(httpRequest, httpResponse, "user/login_failed.html");
            return;
        }
        logger.info("Login success");
        Session newSession = SessionManager.getInstance().createSession();
        newSession.setUser(user);

        int sessionId = newSession.getSessionId();
        Cookie sessionCookie = new Cookie("sid", String.valueOf(sessionId), 0, null,"/", true);
        httpResponse.getHeader().addCookie(sessionCookie);

        ResponseValueSetter.redirect(httpRequest, httpResponse, StaticPage.indexPage);
    }
}
