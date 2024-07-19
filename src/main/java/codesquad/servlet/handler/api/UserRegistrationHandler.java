package codesquad.servlet.handler.api;

import static codesquad.servlet.execption.ErrorCode.ALREADY_REGISTERED_EMAIL;
import static codesquad.servlet.execption.ErrorCode.ALREADY_REGISTERED_USER_ID;
import static codesquad.webserver.http.HttpStatus.OK;

import codesquad.domain.UserStorage;
import codesquad.domain.model.User;
import codesquad.servlet.execption.ClientException;
import codesquad.servlet.handler.Handler;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegistrationHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationHandler.class);
    private final UserStorage userStorage;

    public UserRegistrationHandler(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public void service(final HttpRequest request, final HttpResponse response) {

        String userId = request.getBodyParamValue("userId");
        String password = request.getBodyParamValue("password");
        String name = request.getBodyParamValue("name");
        String email = request.getBodyParamValue("email");

        List<User> findUsers = userStorage.selectAll();
        for (User findUser : findUsers) {
            if (findUser.getUserId().equals(userId)) {
                throw new ClientException("이미 존재하는 userId (%s) 입니다.".formatted(userId), OK,
                        ALREADY_REGISTERED_USER_ID);
            }
            if (findUser.getEmail().equals(email)) {
                throw new ClientException("이미 존재하는 이메일 (%s) 입니다.".formatted(email), OK,
                        ALREADY_REGISTERED_EMAIL);
            }
        }

        User user = new User(userId, password, name, email);
        userStorage.insert(user);
        Optional<User> savedUser = userStorage.selectById(user.getId());
        logger.debug("saved user: {}", savedUser);
        response.sendRedirect("/index.html");
    }

}
