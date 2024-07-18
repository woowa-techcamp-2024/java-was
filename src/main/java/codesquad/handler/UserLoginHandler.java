package codesquad.handler;

import codesquad.database.H2Config;
import codesquad.database.UserRepository;
import codesquad.handler.dto.LoginRequest;
import codesquad.http.HttpCookies;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.session.SessionManager;
import codesquad.model.User;

import java.util.Optional;

public final class UserLoginHandler extends RequestHandler {

    private static UserLoginHandler instance;

    private final ObjectMapper objectMapper = ObjectMapper.getInstance();
    private final UserRepository userRepository = UserRepository.getInstance(H2Config.standard());
    private final SessionManager sessionManager = SessionManager.getInstance();

    private UserLoginHandler() {
    }

    public static UserLoginHandler getInstance() {
        if (instance == null) {
            instance = new UserLoginHandler();
        }
        return instance;
    }

    @Override
    protected HttpResponse handlePost(HttpRequest httpRequest) {
        RequestValidator.validateContentType(httpRequest);

        String body = httpRequest.body()
                .orElseThrow(() -> new IllegalStateException("[ERROR] request body가 없습니다."));
        LoginRequest loginRequest = objectMapper.readQueryString(body, LoginRequest.class);

        Optional<User> user = userRepository.findByUserId(loginRequest.userId());
        if (user.isEmpty() || !user.get().matchPassword(loginRequest.password())) {
            return responseGenerator.sendRedirect(httpRequest, "/login/failed.html");
        }

        String sessionId = sessionManager.createSession(user.get().getUserId());
        HttpResponse httpResponse = responseGenerator.sendRedirect(httpRequest, "/");
        HttpCookies cookie = new HttpCookies();
        cookie.addCookie("sid", sessionId);
        cookie.addCookie("Path", "/");
        httpResponse.addCookie(cookie);
        return httpResponse;
    }
}
