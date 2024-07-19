package codesquad.handler;

import codesquad.database.UserRepository;
import codesquad.error.HttpRequestException;
import codesquad.handler.dto.RegistrationRequest;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.StatusCode;
import codesquad.model.User;

public final class UserRegistrationHandler extends RequestHandler {

    private static UserRegistrationHandler instance;

    private final ObjectMapper objectMapper = ObjectMapper.getInstance();
    private final UserRepository userRepository = UserRepository.getInstance();

    private UserRegistrationHandler() {
    }

    public static UserRegistrationHandler getInstance() {
        if (instance == null) {
            instance = new UserRegistrationHandler();
        }
        return instance;
    }

    @Override
    protected HttpResponse handlePost(HttpRequest httpRequest) {
        RequestValidator.validateContentType(httpRequest);

        String body = httpRequest.body()
                .orElseThrow(() -> new HttpRequestException(StatusCode.BAD_REQUEST, "[ERROR] request body가 없습니다."));
        RegistrationRequest registrationRequest = objectMapper.readQueryString(body, RegistrationRequest.class);

        User user = new User(registrationRequest.userId(), registrationRequest.nickname(), registrationRequest.password());
        if (userRepository.findByUserId(user.getUserId()).isPresent()) {
            throw new HttpRequestException(StatusCode.BAD_REQUEST, "[ERROR] 이미 사용중인 아이디입니다.");
        }
        userRepository.save(user);
        return responseGenerator.sendRedirect(httpRequest, "/");
    }
}
