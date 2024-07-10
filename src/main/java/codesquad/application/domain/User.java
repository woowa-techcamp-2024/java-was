package codesquad.application.domain;

import java.util.Objects;

public class User {
    private final String username;
    private final String password;
    private final String nickname;

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public boolean checkPassword(String password){
        return this.password.equals(password);
    }

    public String getNickname(){
        return nickname;
    }

    public String getUserName(){
        return username;
    }

    @Override
    public String toString() {
        return "User{" +
                "username=" + username + '\'' +
                ", password=" + password + '\'' +
                ", nickname=" + nickname + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(nickname, user.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, nickname);
    }
}
