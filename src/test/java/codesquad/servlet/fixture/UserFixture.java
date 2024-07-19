package codesquad.servlet.fixture;

import codesquad.domain.model.User;

public class UserFixture {

    public static User createUser1() {
        return new User("아이디1", "비밀번호1", "이름1", "email1@woowah.com");
    }

    public static User createUser2() {
        return new User("아이디2", "비밀번호2", "이름2", "email2@woowah.com");
    }

}
