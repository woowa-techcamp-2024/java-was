package codesquad.webserver.dispatcher.requesthandler;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.annotation.Controller;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.db.user.UserDatabase;
import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.dispatcher.view.ModelKey;
import codesquad.webserver.dispatcher.view.ViewName;
import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.db.user.User;
import codesquad.webserver.parser.QueryStringParser;
import codesquad.webserver.session.Session;
import codesquad.webserver.session.SessionManager;
import codesquad.webserver.session.cookie.HttpCookie;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping(path = "/login")
public class LoginRequestHandler extends AbstractRequestHandler {

    private static final String REDIRECT_PATH = "/";
    private static final String LOGIN_FAIL_REDIRECT_PATH = "/login/login_failed";
    private static final String USERNAME_PARAM = "username";
    private static final String PASSWORD_PARAM = "password";
    private static final String COOKIE_NAME = "SID";

    private static final Logger logger = LoggerFactory.getLogger(LoginRequestHandler.class);

    private final UserDatabase userDatabase;
    private final SessionManager sessionManager;

    @Autowired
    public LoginRequestHandler(FileReader fileReader, UserDatabase userDatabase, SessionManager sessionManager) {
        super(fileReader);
        this.userDatabase = userDatabase;
        this.sessionManager = sessionManager;
    }

    @Override
    protected ModelAndView handleGet(HttpRequest request) {
        try {
            FileReader.FileResource file = fileReader.read("/login/index.html");
            return new ModelAndView(ViewName.TEMPLATE_VIEW)
                    .addAttribute(ModelKey.CONTENT, readFileContent(file));
        } catch (IOException e) {
            return new ModelAndView(ViewName.TEMPLATE_VIEW)
                    .addAttribute(ModelKey.STATUS_CODE, 404)
                    .addAttribute(ModelKey.CONTENT, "Login page not found");
        }
    }

    @Override
    protected ModelAndView handlePost(HttpRequest request) {
        QueryStringParser.parse(request.getBody());
        Map<String, String> params = request.getParams();
        String username = params.get(USERNAME_PARAM);
        String password = params.get(PASSWORD_PARAM);

        try {
            User user = userDatabase.findByUserId(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
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

    private ModelAndView createSuccessResponse(User user) {
        Session session = sessionManager.createSession(user);
        logger.debug("세션 생성 성공 {}", session.getId());

        HttpCookie sessionCookie = new HttpCookie(COOKIE_NAME, session.getId())
                .setHttpOnly(true)
                .setMaxAge(30 * 24 * 60 * 60);

        return new ModelAndView(ViewName.REDIRECT_VIEW)
                .addAttribute(ModelKey.REDIRECT_URL, REDIRECT_PATH)
                .addAttribute(ModelKey.COOKIES, List.of(sessionCookie));
    }

    private ModelAndView createFailureResponse() {
        return new ModelAndView(ViewName.REDIRECT_VIEW)
                .addAttribute(ModelKey.REDIRECT_URL, LOGIN_FAIL_REDIRECT_PATH);
    }

    private String readFileContent(FileReader.FileResource file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}