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
import java.util.Map;

@Controller
@RequestMapping(path = "/create")
public class UserCreateRequestHandler extends AbstractRequestHandler {

    private static final String HOME_PATH = "/";

    private final UserDatabase userDatabase;

    @Autowired
    public UserCreateRequestHandler(FileReader fileReader, UserDatabase userDatabase) {
        super(fileReader);
        this.userDatabase = userDatabase;
    }

    @Override
    protected ModelAndView handlePost(HttpRequest request) {
        Map<String, String> params = QueryStringParser.parse(request.getBody());
        User user = User.of(params);

        ModelAndView mv = new ModelAndView(ViewName.REDIRECT_VIEW);
        try {
            userDatabase.save(user);
            mv.addAttribute(ModelKey.REDIRECT_URL, HOME_PATH);
        } catch (IllegalArgumentException e) {
            //TODO: 회원가입 실패 패이지로 전달. handler도 추가
            mv.addAttribute(ModelKey.REDIRECT_URL, HOME_PATH);
        }
        return mv;
    }
}
