package codesquad.servlet.handler.api;

import codesquad.domain.InMemoryUserStorage;
import codesquad.domain.model.User;
import codesquad.servlet.handler.Handler;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegistrationHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationHandler.class);
    private final InMemoryUserStorage inMemoryUserStorage;

    public UserRegistrationHandler(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @Override
    public void service(final HttpRequest request, final HttpResponse response) {

        String userId = request.getBodyParamValue("userId");
        String password = request.getBodyParamValue("password");
        String name = request.getBodyParamValue("name");
        String email = request.getBodyParamValue("email");
        User user = new User(userId, password, name, email);
        inMemoryUserStorage.save(user);
        Optional<User> savedUser = inMemoryUserStorage.findById(user.getUserId());
        logger.debug("User Registration Success = {}", savedUser);
        response.sendRedirect("/index.html");
    }

}
