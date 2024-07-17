package codesquad.webserver.dispatcher.requesthandler;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.annotation.Component;
import codesquad.webserver.annotation.Controller;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.dispatcher.view.ModelKey;
import codesquad.webserver.dispatcher.view.ViewName;
import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.session.SessionManager;
import codesquad.webserver.session.cookie.HttpCookie;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping(path = "/logout")
public class LogoutRequestHandler extends AbstractRequestHandler {

    private static final String REDIRECT_PATH = "/";
    private static final String COOKIE_NAME = "SID";

    private static final Logger logger = LoggerFactory.getLogger(LogoutRequestHandler.class);

    private final SessionManager sessionManager;

    @Autowired
    public LogoutRequestHandler(FileReader fileReader, SessionManager sessionManager) {
        super(fileReader);
        this.sessionManager = sessionManager;
    }

    @Override
    protected ModelAndView handlePost(HttpRequest request) {
        try {
            String sessionId = request.getSessionIdFromRequest();
            sessionManager.invalidateSession(sessionId);

            HttpCookie expiredCookie = new HttpCookie(COOKIE_NAME, "")
                    .setMaxAge(0)
                    .setHttpOnly(true);

            return new ModelAndView(ViewName.REDIRECT_VIEW)
                    .addAttribute(ModelKey.REDIRECT_URL, REDIRECT_PATH)
                    .addAttribute(ModelKey.COOKIES, List.of(expiredCookie));
        } catch (IllegalStateException e) {
            logger.error("Session not found", e);
            return new ModelAndView(ViewName.EXCEPTION_VIEW)
                    .addAttribute(ModelKey.STATUS_CODE, 404)
                    .addAttribute(ModelKey.ERROR_MESSAGE, "Session not found");
        }
    }
}