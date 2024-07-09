package codesquad.handler;

import codesquad.http.HttpHeaders;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.QueryParameters;
import codesquad.http.parser.ParsersFactory;
import codesquad.http.parser.QueryParametersParser;
import codesquad.model.User;
import codesquad.model.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UserRegistrationHandler extends RequestHandler {

    private static UserRegistrationHandler INSTANCE = new UserRegistrationHandler();

    private final Logger log = LoggerFactory.getLogger(UserRegistrationHandler.class);

    private final UserRepository userRepository = new UserRepository();
    private final QueryParametersParser queryParametersParser = ParsersFactory.getQueryParametersParser();

    private UserRegistrationHandler() {
    }

    public static UserRegistrationHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserRegistrationHandler();
        }
        return INSTANCE;
    }

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        if (!httpRequest.method().equals("POST")) {
            // TODO 405 Method Not Allowed 적용
            throw new IllegalArgumentException("[ERROR] 회원가입 요청은 POST 메서드여야 합니다.");
        }

        String contentType = httpRequest.firstHeaderValue(HttpHeaders.CONTENT_TYPE)
                .orElse("");
        if (!contentType.equals("application/x-www-form-urlencoded")) {
            // TODO 400 Bad Request 적용
            throw new IllegalArgumentException("[ERROR] request body 타입이 올바르지 않습니다.");
        }

        String body = httpRequest.body()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] request body가 없습니다."));
        QueryParameters parameters = queryParametersParser.parse(body);

        String userId = parameters.getFirstValue("userId")
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 회원가입 아이디가 비어있습니다."));
        String nickname = parameters.getFirstValue("nickname")
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 회원가입 닉네임이 비어있습니다."));
        String password = parameters.getFirstValue("password")
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 회원가입 비밀번호가 비어있습니다."));

        User user = new User(userId, nickname, password);
        boolean success = userRepository.save(user);
        if (success) {
            return responseGenerator.sendRedirect(httpRequest, "/");
        }
        return responseGenerator.sendBadRequest(httpRequest);
    }

}
