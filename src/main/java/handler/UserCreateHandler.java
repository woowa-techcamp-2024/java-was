package handler;

import dto.UserDto;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseWriter;
import http.startline.RequestLine;
import java.util.Map;
import service.UserService;

public class UserCreateHandler extends MyHandler {
    // TODO: 이런식으로 json 형태로 요청이나 응답을 보내는 경우
    // TODO: Content-Type을 application/json으로 설정하는 클래스를 하나 더 만들 계획
    private final UserService userService;

    public UserCreateHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        RequestLine startLine = (RequestLine) httpRequest.getStartLine();

        Map<String, String> queryParams = startLine.getUrlPath().getQueryParameters();
        UserDto userDto = new UserDto(
            queryParams.get("userId"),
            queryParams.get("password"),
            queryParams.get("name"),
            queryParams.get("email")
        );
        userService.createUser(userDto);
        httpResponse.getHeader().addHeader("Content-Type", "text/html");

        ResponseWriter.redirect(httpRequest, httpResponse, "/index.html");
    }

    @Override
    void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {

    }
}
