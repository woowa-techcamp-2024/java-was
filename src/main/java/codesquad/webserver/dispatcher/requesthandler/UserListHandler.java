package codesquad.webserver.dispatcher.requesthandler;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.annotation.Component;
import codesquad.webserver.db.user.UserDatabase;
import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.dispatcher.view.ModelKey;
import codesquad.webserver.dispatcher.view.ViewName;
import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.filereader.FileReader.FileResource;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.model.User;
import codesquad.webserver.session.Session;
import codesquad.webserver.session.SessionManager;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UserListHandler extends AbstractRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserListHandler.class);
    private static final String PATH = "/user/user_list.html";

    private final UserDatabase userDatabase;
    private final SessionManager sessionManager;
    @Autowired
    public UserListHandler(FileReader fileReader, UserDatabase userDatabase, SessionManager sessionManager) {
        super(fileReader);
        this.userDatabase = userDatabase;
        this.sessionManager = sessionManager;
    }

    @Override
    protected ModelAndView handleGet(HttpRequest request) {
        // 필터에서 세션 체크 이미 끝난 상태
        List<User> users = userDatabase.findAllUsers();
        ModelAndView mv = new ModelAndView(ViewName.TEMPLATE_VIEW);
        try {
            FileResource fileResource = fileReader.read(PATH);
            String content = fileResource.readFileContent();
            //-----------------------------
            String sessionId = null;
            try {
                sessionId = request.getSessionIdFromRequest();
            } catch (IllegalStateException e) {
                logger.info("세션ID를 찾을 수 없는 요청입니다.: {}", e.getMessage());
            }

            User user = null;
            if (sessionId != null) {
                user = Optional.ofNullable(sessionManager.getSession(sessionId))
                        .map(Session::getUser)
                        .orElse(null);
            }

            if (user != null) {
                mv.addAttribute("isLoggedIn", true);
                mv.addAttribute("username", user.getName());
            } else {
                mv.addAttribute("isLoggedIn", false);
            }
            // -----------------반복 사용됨. 리팩토링 필요----------

            mv.addAttribute(ModelKey.USERS, users);
            mv.addAttribute(ModelKey.CONTENT, content);
            return mv;
        } catch (IOException e) {
            logger.error("error reading file", e);
            return new ModelAndView(ViewName.EXCEPTION_VIEW)
                    .addAttribute(ModelKey.STATUS_CODE, 500)
                    .addAttribute(ModelKey.ERROR_MESSAGE, "Internal server error");
        }
    }
}
