package handler;

import static util.HeaderStringUtil.CONTENT_TYPE;

import dto.UserDto;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import java.util.Map;
import org.slf4j.Logger;
import service.UserService;
import util.LoggerUtil;
import util.StaticPage;

public class UserCreateHandler extends MyHandler {
    private static final Logger logger = LoggerUtil.getLogger();

    // TODO: 이런식으로 json 형태로 요청이나 응답을 보내는 경우
    // TODO: Content-Type을 application/json으로 설정하는 클래스를 하나 더 만들 계획
    private final UserService userService;

    public UserCreateHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        logger.info("UserCreateHandler doPost");
        Map<String, String> bodyParams = httpRequest.bodyParamsString();
        UserDto userDto = new UserDto(
            bodyParams.get("userId"),
            bodyParams.get("password"),
            bodyParams.get("name"),
            bodyParams.get("email")
        );
        userService.createUser(userDto);
        httpResponse.getHeader().addKey(CONTENT_TYPE, "text/html");
        logger.info("redirect to {}", StaticPage.indexPage);
        ResponseValueSetter.redirect(httpRequest, httpResponse, StaticPage.indexPage);
    }
}
