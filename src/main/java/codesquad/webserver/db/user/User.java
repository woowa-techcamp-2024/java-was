package codesquad.webserver.db.user;

import java.util.Map;

public class User {
    private final String userId;
    private final String password;
    private final String name;

    public User(String userId, String password, String name) {
        this.userId = userId;
        this.password = password;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public static User of(Map<String, String> userInfo) {
        return new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"));
    }
}
