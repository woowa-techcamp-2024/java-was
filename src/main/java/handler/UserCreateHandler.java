package handler;

import dto.UserDto;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import java.util.Map;
import java.util.logging.Logger;
import service.UserService;

public class UserCreateHandler extends MyHandler {
    private final Logger logger = Logger.getLogger(UserCreateHandler.class.getName());

    // TODO: 이런식으로 json 형태로 요청이나 응답을 보내는 경우
    // TODO: Content-Type을 application/json으로 설정하는 클래스를 하나 더 만들 계획
    private final UserService userService;

    public UserCreateHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        logger.info("UserCreateHandler doPost");
        Map<String, String> bodyParams = httpRequest.getBodyParams();
        UserDto userDto = new UserDto(
            bodyParams.get("userId"),
            bodyParams.get("password"),
            bodyParams.get("name"),
            bodyParams.get("email")
        );
        userService.createUser(userDto);
        httpResponse.getHeader().addKey("Content-Type", "text/html");
        logger.info("redirect to /index.html");
        ResponseValueSetter.redirect(httpRequest, httpResponse, "/index.html");
    }
}
