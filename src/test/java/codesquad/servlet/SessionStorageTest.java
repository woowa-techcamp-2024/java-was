package codesquad.servlet;

import codesquad.domain.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SessionStorageTest {

    SessionStorage sessionStorage = SessionStorage.getInstance();

    @Nested
    @DisplayName("세션을 생성하는 기능은")
    class CreateSessionTest {

        @Test
        @DisplayName("[Success] key:UUID, value:User로 저장한다.")
        void create() {
            // given
            User user = new User("아이디", "비밀번호", "이름", "이메일");
            String sessionKey = sessionStorage.createSession(user);
            // when
            User sessionValue = sessionStorage.getSessionValue(sessionKey);
            // then
            Assertions.assertThat(user).isEqualTo(sessionValue);
        }

    }
}