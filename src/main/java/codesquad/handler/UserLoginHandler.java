package codesquad.handler;

import codesquad.database.SessionDatabase;
import codesquad.database.UserH2Database;
import codesquad.file.FileReader;
import codesquad.http.HttpStatus;
import codesquad.http.request.HttpRequest;
import codesquad.http.response.HttpResponse;
import codesquad.util.DirectoryMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserLoginHandler implements Handler {

    private final UserH2Database userH2Database = new UserH2Database();
    private final SessionDatabase sessionDatabase = new SessionDatabase();
    private final String LOGIN_FAILED_URL = "/user/login_failed.html";

    public UserLoginHandler() {
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws RuntimeException {
        if (request.method().equals("POST")) {
            return doPost(request);
        } else if (request.method().equals("GET")) {
            return doGet(request);
        }
        return new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED, "text", new byte[0]);
    }

    public HttpResponse doGet(HttpRequest request) throws RuntimeException {
        String mappedPath = DirectoryMapper.getStaticResourcePath(request.path());
        if (mappedPath != null) {
            return new HttpResponse(HttpStatus.OK, mappedPath, FileReader.getContent(mappedPath));
        }
        HttpResponse response = new HttpResponse(HttpStatus.OK, request.path(), FileReader.getContent(request.path()));
        response.addHeader("Content-Type", "text/html");
        return new HttpResponse(HttpStatus.OK, request.path(), FileReader.getContent(request.path()));
    }

    public HttpResponse doPost(HttpRequest request) {
        Map<String, String> stringMap = parseUserInfo(new String(request.body()));
        String userId = stringMap.get("userId");
        String password = stringMap.get("password");

        if(userId == null || password == null)
            return new HttpResponse(HttpStatus.BAD_REQUEST, "text", "".getBytes());

        return userH2Database.findUserById(userId)
                .filter(user -> user.getPassword().equals(password))
                .map(user -> {
                    HttpResponse response = new HttpResponse("/");
                    String uuid = UUID.randomUUID().toString();
                    setCookie(response, uuid);
                    sessionDatabase.addSession(uuid, user);
                    return response;
                })
                .orElse(new HttpResponse(LOGIN_FAILED_URL));
    }

    private Map<String, String> parseUserInfo(String body) {

        Map<String, String> userInfo = new HashMap<>();
        // 쿼리 문자열을 파라미터로 분리
        String[] params = body.split("&");

        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                userInfo.put(keyValue[0], keyValue[1]);
            }
        }
        return userInfo;
    }

    private void setCookie(HttpResponse response, String uuid) {
        response.addHeader("Set-Cookie", "sid=" + uuid + "; Path=/");
    }

}