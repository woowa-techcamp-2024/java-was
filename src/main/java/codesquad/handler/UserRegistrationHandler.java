package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.model.User;
import codesquad.model.UserRepository;

public class UserRegistrationHandler extends RequestHandler {

    private final UserRepository userRepository = new UserRepository();

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        String userId = httpRequest.firstParameterValue("userId")
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 회원가입 아이디가 비어있습니다."));
        String nickname = httpRequest.firstParameterValue("nickname")
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 회원가입 닉네임이 비어있습니다."));
        String password = httpRequest.firstParameterValue("password")
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 회원가입 비밀번호가 비어있습니다."));

        User user = new User(userId, nickname, password);
        boolean success = userRepository.save(user);
        if (success) {
            return responseGenerator.sendRedirect(httpRequest, "/login");
        }
        return responseGenerator.sendBadRequest(httpRequest);
    }

}
