package codesquad.servlet.handler.api;

import codesquad.domain.UserStorage;
import codesquad.domain.model.User;
import codesquad.servlet.handler.Handler;
import codesquad.webserver.http.HttpMediaType;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllUserInfoHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(AllUserInfoHandler.class);
    private final UserStorage userStorage;

    public AllUserInfoHandler(UserStorage userStorage) {
        this.userStorage = userStorage;
    }
    
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        logger.debug("AllUserInfoHandler start");
        List<User> users = userStorage.selectAll();
        List<UserInfo> userInfos = new ArrayList<>();
        for (User user : users) {
            userInfos.add(new UserInfo(user));
        }
        String json = listToJson(userInfos);
        logger.debug("AllUserInfoHandler end");
        response.setContentType(HttpMediaType.APPLICATION_JSON);
        response.setBody(json.getBytes());
    }

    private String listToJson(List<UserInfo> userInfoList) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < userInfoList.size(); i++) {
            sb.append(userInfoList.get(i).toString());
            if (i < userInfoList.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
