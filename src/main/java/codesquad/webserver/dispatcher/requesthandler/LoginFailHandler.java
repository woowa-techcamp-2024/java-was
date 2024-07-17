package codesquad.webserver.dispatcher.requesthandler;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.annotation.Component;
import codesquad.webserver.annotation.Controller;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.db.user.UserDatabase;
import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.dispatcher.view.ModelKey;
import codesquad.webserver.dispatcher.view.ViewName;
import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.session.SessionManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping(path = "/login/login_failed")
public class LoginFailHandler extends AbstractRequestHandler {

    private static final String LOGIN_FAIL_REDIRECT_PATH = "/login/login_failed.html";

    private static final Logger logger = LoggerFactory.getLogger(LoginFailHandler.class);

    @Autowired
    public LoginFailHandler(FileReader fileReader, UserDatabase userDatabase, SessionManager sessionManager) {
        super(fileReader);
    }

    @Override
    protected ModelAndView handleGet(HttpRequest request) {
        try {
            FileReader.FileResource file = fileReader.read(LOGIN_FAIL_REDIRECT_PATH);
            return new ModelAndView(ViewName.TEMPLATE_VIEW)
                    .addAttribute(ModelKey.CONTENT, readFileContent(file));
        } catch (IOException e) {
            return new ModelAndView(ViewName.TEMPLATE_VIEW)
                    .addAttribute(ModelKey.STATUS_CODE, 404)
                    .addAttribute(ModelKey.CONTENT, "Login page not found");
        }
    }


    private String readFileContent(FileReader.FileResource file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}