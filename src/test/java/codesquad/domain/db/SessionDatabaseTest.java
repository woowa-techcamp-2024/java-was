package codesquad.domain.db;

import codesquad.domain.entity.Session;
import codesquad.domain.entity.User;
import codesquad.security.SessionManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static codesquad.Main.sessionDatabase;
import static org.junit.jupiter.api.Assertions.*;

public class SessionDatabaseTest {
    @Test
    @DisplayName("session 정보를 입력하면 이에 해당하는 session 객체가 생성됩니다")
    void insertUser() {
        User user = new User("user1", "nickname1", "password1");
        Session session = sessionDatabase.insert(user);
        assertEquals(user, sessionDatabase.selectById(session));
    }

    @Test
    @DisplayName("해당 session이 존재하는지 확인할 수 있습니다")
    void existSession() {
        User user = new User("userId1", "nickName1", "password1");

        Session session1 = sessionDatabase.insert(user);
        Session session2 = SessionManager.createSession();

        assertTrue(SessionManager.isExist(session1));
        assertFalse(SessionManager.isExist(session2));
    }
}
