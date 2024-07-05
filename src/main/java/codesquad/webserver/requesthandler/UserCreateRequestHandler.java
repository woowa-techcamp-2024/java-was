package codesquad.webserver.requesthandler;

import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.db.UserDatabase;
import codesquad.webserver.db.UserDatabaseFactory;
import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.httpresponse.HttpResponseBuilder;
import codesquad.webserver.model.User;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserCreateRequestHandler extends AbstractRequestHandler{

    private static final String HOME_PATH = "/index.html";
    private static final Logger logger = LoggerFactory.getLogger(UserCreateRequestHandler.class);

    public UserCreateRequestHandler(FileReader fileReader) {
        super(fileReader);
    }

    @Override
    protected HttpResponse handleGet(HttpRequest request) {
        UserDatabase userDatabase = UserDatabaseFactory.getInstance();
        Map<String, String> params = request.params();
        User user = User.of(params);

        userDatabase.save(user);
        logger.debug("사용자 저장 : {}", user);
        return HttpResponseBuilder.buildRedirectResponse(HOME_PATH);
    }
}
