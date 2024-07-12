package codesquad.servlet.handler.api;

import codesquad.domain.model.User;

public class UserInfo {

    private final String userName;
    private final String userEmail;

    public UserInfo(User user) {
        this.userName = user.getName();
        this.userEmail = user.getEmail();
    }

    @Override
    public String toString() {
        return "{" +
                "\"userName\" : \"" + userName + "\"," +
                "\"userEmail\" : \"" + userEmail + "\"" +
                "}";
    }

}
