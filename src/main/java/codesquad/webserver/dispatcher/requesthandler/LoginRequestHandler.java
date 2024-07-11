package codesquad.webserver.dispatcher.requesthandler;

import codesquad.webserver.db.session.Session;
import codesquad.webserver.db.session.SessionManager;
import codesquad.webserver.db.user.UserDatabase;
import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.httpresponse.HttpResponseBuilder;
import codesquad.webserver.model.User;
import codesquad.webserver.parser.QueryStringParser;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginRequestHandler extends AbstractRequestHandler {

    private static final String REDIRECT_PATH = "/index.html";
    private static final String LOGIN_FAIL_REDIRECT_PATH = "/user/login_failed.html";
    private static final String USERNAME_PARAM = "username";
    private static final String PASSWORD_PARAM = "password";
    private static final String COOKIE_NAME = "JID";
    private static final String COOKIE_PATH = "Path=/";

    private static final Logger logger = LoggerFactory.getLogger(LoginRequestHandler.class);

    private final UserDatabase userDatabase;
    private final SessionManager sessionManager;

    public LoginRequestHandler(FileReader fileReader, UserDatabase userDatabase, SessionManager sessionManager) {
        super(fileReader);
        this.userDatabase = userDatabase;
        this.sessionManager = sessionManager;
    }

    @Override
    protected HttpResponse handlePost(HttpRequest request) {
        Map<String, String> params = QueryStringParser.parse(request.body());
        String username = params.get(USERNAME_PARAM);
        String password = params.get(PASSWORD_PARAM);

        try {
            User user = userDatabase.findByUserId(username);
            if (isValidPassword(user, password)) {
                logger.info("로그인 성공: {}", username);
                return createSuccessResponse(user);
            } else {
                logger.warn("비밀번호 불일치: {}", username);
                return createFailureResponse();
            }
        } catch (IllegalArgumentException e) {
            logger.warn("사용자 없음: {}", username);
            return createFailureResponse();
        }
    }

    private boolean isValidPassword(User user, String inputPassword) {
        return user.getPassword().equals(inputPassword);
    }

    private HttpResponse createSuccessResponse(User user) {
        Session session = sessionManager.createSession(user);
        logger.debug("세선 생성 성공 {}", session.getId());
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Set-Cookie", Arrays.asList(COOKIE_NAME + "=" + session.getId(), COOKIE_PATH));
        return HttpResponseBuilder.buildRedirectResponse(REDIRECT_PATH, headers);
    }

    private HttpResponse createFailureResponse() {
        return HttpResponseBuilder.buildRedirectResponse(LOGIN_FAIL_REDIRECT_PATH);
    }
}