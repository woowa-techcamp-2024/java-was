package codesquad.model;

public class User {

    private final Long id;
    private final String userId;
    private final String nickname;
    private final String password;

    public User(Long id, String userId, String nickname, String password) {
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.password = password;
    }

    public User(String userId, String nickname, String password) {
        this(null, userId, nickname, password);
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }
}
