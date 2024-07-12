package codesquad.handler;

import codesquad.error.HttpStatusException;
import codesquad.handler.dto.RegistrationRequest;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.StatusCode;
import codesquad.model.User;
import codesquad.model.UserDataBase;

public final class UserRegistrationHandler extends RequestHandler {

    private static UserRegistrationHandler instance;

    private final ObjectMapper objectMapper = ObjectMapper.getInstance();
    private final UserDataBase userDataBase = UserDataBase.getInstance();

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
                .orElseThrow(() -> new HttpStatusException(StatusCode.BAD_REQUEST, "[ERROR] request body가 없습니다."));
        RegistrationRequest registrationRequest = objectMapper.readQueryString(body, RegistrationRequest.class);

        User user = new User(registrationRequest.userId(), registrationRequest.nickname(), registrationRequest.password());
        boolean success = userDataBase.addUser(user);
        if (success) {
            return responseGenerator.sendRedirect(httpRequest, "/");
        }
        throw new HttpStatusException(StatusCode.BAD_REQUEST, "[ERROR] 이미 사용중인 아이디입니다.");
    }
}
