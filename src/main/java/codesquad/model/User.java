package codesquad.model;

public class User {

    private final String userId;
    private final String nickname;
    private final String password;

    public User(String userId, String nickname, String password) {
        this.userId = userId;
        this.nickname = nickname;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

}
