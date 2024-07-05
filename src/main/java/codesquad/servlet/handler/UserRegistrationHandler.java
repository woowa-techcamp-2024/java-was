package codesquad.servlet.handler;

import codesquad.model.User;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegistrationHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationHandler.class);

    @Override
    public void service(final HttpRequest request, final HttpResponse response) {
        Map<String, String> queryStrings = request.getQueryStrings();

        String userId = queryStrings.get("userId");
        String password = queryStrings.get("password");
        String name = queryStrings.get("name");
        String email = queryStrings.get("email");
        User user = new User(userId, password, name, email);
        logger.debug("User Registration Success = {}", user);
        response.sendRedirect("/");
    }

}
