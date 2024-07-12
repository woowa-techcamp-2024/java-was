package codesquad.servlet.handler.api;

import codesquad.domain.InMemoryUserStorage;
import codesquad.domain.model.User;
import codesquad.servlet.SessionStorage;
import codesquad.servlet.handler.Handler;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegistrationHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationHandler.class);
    private final InMemoryUserStorage inMemoryUserStorage;
    private final SessionStorage sessionStorage;

    public UserRegistrationHandler(InMemoryUserStorage inMemoryUserStorage, SessionStorage sessionStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.sessionStorage = sessionStorage;
    }

    @Override
    public void service(final HttpRequest request, final HttpResponse response) {

        String userId = request.getBodyParamValue("userId");
        String password = request.getBodyParamValue("password");
        String name = request.getBodyParamValue("name");
        String email = request.getBodyParamValue("email");

        List<User> findUsers = inMemoryUserStorage.findAll();
        for (User findUser : findUsers) {
            if (findUser.getUserId().equals(userId)) {
                throw new IllegalArgumentException("이미 존재하는 userId (%s) 입니다.".formatted(userId));
            }
            if (findUser.getEmail().equals(email)) {
                throw new IllegalArgumentException("이미 존재하는 이메일 (%s) 입니다.".formatted(email));
            }
        }

        User user = new User(userId, password, name, email);
        inMemoryUserStorage.save(user);
        Optional<User> savedUser = inMemoryUserStorage.findById(user.getUserId());
        logger.debug("saved user: {}", savedUser);
        response.sendRedirect("/index.html");
    }

}
