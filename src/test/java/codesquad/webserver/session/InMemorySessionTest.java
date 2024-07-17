package codesquad.webserver.session;

import codesquad.webserver.db.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class InMemorySessionTest {

    private InMemorySession session;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("1", "password", "테스트 유저");
        session = new InMemorySession("sessionId", user);
    }

    @Test
    @DisplayName("세션 ID를 올바르게 반환한다")
    void testGetId() {
        assertThat(session.getId()).isEqualTo("sessionId");
    }

    @Test
    @DisplayName("사용자 객체를 올바르게 반환한다")
    void testGetUser() {
        assertThat(session.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("세션 속성을 설정하고 올바르게 반환한다")
    void testSetAndGetAttribute() {
        session.setAttribute("key", "value");
        assertThat(session.getAttribute("key")).isEqualTo("value");
    }

    @Test
    @DisplayName("존재하지 않는 세션 속성을 조회하면 null을 반환한다")
    void testGetNonExistingAttribute() {
        assertThat(session.getAttribute("nonexistent")).isNull();
    }

    @Test
    @DisplayName("세션 속성을 제거한다")
    void testRemoveAttribute() {
        session.setAttribute("key", "value");
        session.removeAttribute("key");
        assertThat(session.getAttribute("key")).isNull();
    }

    @Test
    @DisplayName("세션 생성 시간을 올바르게 반환한다")
    void testGetCreationTime() {
        long currentTime = System.currentTimeMillis();
        long creationTime = session.getCreationTime();
        assertThat(creationTime).isLessThanOrEqualTo(currentTime);
    }

    @Test
    @DisplayName("세션을 무효화한다")
    void testInvalidate() {
        session.setAttribute("key", "value");
        session.invalidate();
        assertThat(session.getAttribute("key")).isNull();
        assertThat(session.getAttribute("anotherKey")).isNull(); // 속성이 비어있음을 확인
    }
}
