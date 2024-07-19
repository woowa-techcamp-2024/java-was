package codesquad.servlet.database.connection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import codesquad.configuration.TestDatabaseConfiguration;
import codesquad.domain.model.User;
import codesquad.helper.TestDatabaseExtension;
import codesquad.servlet.database.exception.InvalidDataAccessException;
import codesquad.servlet.database.exception.InvalidDataSourceException;
import codesquad.servlet.database.property.DataSourceProvider;
import codesquad.servlet.fixture.UserFixture;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

class UserDaoTest {

    @Nested
    @DisplayName("Describe_CRUD 테스트")
    @ExtendWith(TestDatabaseExtension.class)
    class CRUD_Test {

        DatabaseConnector databaseConnector = TestDatabaseConfiguration.getDatabaseConnector();

        UserDao userDao = new UserDao(databaseConnector);
        User user1 = UserFixture.createUser1();
        User user2 = UserFixture.createUser2();

        @AfterEach
        void cleanUserDatabase() {
            try {
                userDao.deleteById(user1.getId());
                userDao.deleteById(user2.getId());
            } catch (InvalidDataAccessException e) {
                System.out.println("After Each Catch = " + e);
                // ignore
            }
        }

        @Test
        @DisplayName("[Success] User를 Insert 하면 DB에 저장된다")
        void test() {
            Long id = userDao.insert(user1);
            Optional<User> findUser = userDao.selectById(id);

            assertThat(findUser).isPresent();
            User user = findUser.get();
            assertUser(user, user1);
        }

        @Test
        @DisplayName("[Success] id 값으로 Delete 하면 DB에서 삭제된다")
        void test2() {
            Long id = userDao.insert(user1);
            userDao.deleteById(id);

            Optional<User> findUser = userDao.selectById(user1.getId());
            assertThat(findUser).isNotPresent();
        }

        @Test
        @DisplayName("[Exception] 존재하지 않는 id 값으로 Delete 하면 InvalidDataAccess 예외가 발생한다")
        void test3() {
            assertThatThrownBy(() -> userDao.deleteById(123123L)).isInstanceOf(InvalidDataAccessException.class);
        }

        @Test
        @DisplayName("[Success] 모든 사용자를 조회한다")
        void test4() {
            userDao.insert(user1);
            userDao.insert(user2);

//            List<User> users = userDao.selectAll();
//            assertThat(users).hasSize(2);
//            assertUser(users.get(0), user1);
//            assertUser(users.get(1), user2);
        }

        private void assertUser(User user, User expectedUser) {
            assertThat(user.getUserId()).isEqualTo(expectedUser.getUserId());
            assertThat(user.getName()).isEqualTo(expectedUser.getName());
            assertThat(user.getEmail()).isEqualTo(expectedUser.getEmail());
            assertThat(user.getPassword()).isEqualTo(expectedUser.getPassword());
        }

        @Test
        @DisplayName("[Success] 존재하지 않는 id 값으로 사용자를 조회하면 비어있는 Optional을 반환한다")
        void test5() {
            Optional<User> article = userDao.selectById(12312L);
            assertThat(article).isEmpty();
        }
    }

    @Nested
    @DisplayName("Describe_데이터베이스 커넥션 테스트")
    class DatabaseConnectionTest {

        User user1 = UserFixture.createUser1();

        @Test
        @DisplayName("[Exception] 잘못된 DataSource 정보면 DB 커넥션을 획득하는데 실패한다")
        void test() {
            // given
            DataSourceProvider dataSourceProvider = new DataSourceProvider(
                    "이상한JdbcURL", "이상한사용자이름", "이상한비밀번호");
            DatabaseConnector databaseConnector = new DatabaseConnector(dataSourceProvider);
            UserDao userDao = new UserDao(databaseConnector);
            user1.setPrimaryKey(1L);

            // when then
            assertThatThrownBy(() -> userDao.insert(user1)).isInstanceOf(InvalidDataSourceException.class);
            assertThatThrownBy(() -> userDao.selectById(user1.getId())).isInstanceOf(InvalidDataSourceException.class);
            assertThatThrownBy(() -> userDao.deleteById(user1.getId())).isInstanceOf(InvalidDataSourceException.class);
        }
    }

}