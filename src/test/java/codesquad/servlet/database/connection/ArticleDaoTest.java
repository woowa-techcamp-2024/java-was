package codesquad.servlet.database.connection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import codesquad.configuration.TestDatabaseConfiguration;
import codesquad.domain.model.Article;
import codesquad.domain.model.User;
import codesquad.helper.TestDatabaseExtension;
import codesquad.servlet.database.exception.InvalidDataAccessException;
import codesquad.servlet.database.exception.InvalidDataSourceException;
import codesquad.servlet.database.property.DataSourceProvider;
import codesquad.servlet.fixture.ArticleFixture;
import codesquad.servlet.fixture.UserFixture;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

class ArticleDaoTest {


    @Nested
    @DisplayName("Describe_CRUD 테스트")
    @ExtendWith(TestDatabaseExtension.class)
    class CRUD_Test {

        DatabaseConnector databaseConnector = TestDatabaseConfiguration.getDatabaseConnector();

        UserDao userDao;
        ArticleDao articleDao;

        User user1;
        User user2;

        Article article1;
        Article article2;

        @BeforeEach
        void initUser() {
            userDao = new UserDao(databaseConnector);
            articleDao = new ArticleDao(databaseConnector);

            user1 = UserFixture.createUser1();
            user2 = UserFixture.createUser2();

            userDao.insert(user1);
            userDao.insert(user2);

            article1 = ArticleFixture.createArticle1(user1.getId());
            article2 = ArticleFixture.createArticle2(user2.getId());
        }

        @AfterEach
        void cleanUserDatabase() {
            try {
                articleDao.deleteById(article1.getId());
                articleDao.deleteById(article2.getId());

                userDao.deleteById(user1.getId());
                userDao.deleteById(user2.getId());

            } catch (InvalidDataAccessException e) {
                System.out.println("After Each Catch = " + e);
                // ignore
            }
        }

        @Test
        @DisplayName("[Success] Article을 Insert 하면 DB에 저장된다")
        void test() {
            Long id = articleDao.insert(article1);
            Optional<Article> optFindArticle = articleDao.selectById(id);
            assertThat(optFindArticle.isPresent()).isTrue();
            Article findArticle = optFindArticle.get();

            assertArticle(findArticle, article1);
        }

        @Test
        @DisplayName("[Success] id 값으로 Delete 하면 DB에서 삭제된다")
        void test2() {
            Long id = articleDao.insert(article1);
            articleDao.deleteById(id);

            Optional<Article> findUser = articleDao.selectById(article1.getId());
            assertThat(findUser).isNotPresent();
        }

        @Test
        @DisplayName("[Exception] 존재하지 않는 id 값으로 Delete 하면 InvalidDataAccess 예외가 발생한다")
        void test3() {
            assertThatThrownBy(() -> articleDao.deleteById(123123L)).isInstanceOf(InvalidDataAccessException.class);
        }

        @Test
        @DisplayName("[Success] 모든 게시글을 조회한다")
        void test4() {
            articleDao.insert(article1);
            articleDao.insert(article2);

            List<Article> articles = articleDao.selectAll();
            assertThat(articles).hasSize(2);
            assertArticle(articles.get(0), article1);
            assertArticle(articles.get(1), article2);
        }

        private void assertArticle(Article findArticle, Article expected) {
            assertThat(findArticle.getContent()).isEqualTo(expected.getContent());
            assertThat(findArticle.getTitle()).isEqualTo(expected.getTitle());
            assertThat(findArticle.getAuthorId()).isEqualTo(expected.getAuthorId());
        }

        @Test
        @DisplayName("[Success] 존재하지 않는 id 값으로 게시글을 조회하면 비어있는 Optional을 반환한다")
        void test5() {
            Optional<Article> article = articleDao.selectById(12312L);
            assertThat(article).isEmpty();
        }
    }

    @Nested
    @DisplayName("Describe_데이터베이스 커넥션 테스트")
    class DatabaseConnectionTest {

        User user1 = UserFixture.createUser1();
        Article article1 = ArticleFixture.createArticle1(user1.getId());

        @Test
        @DisplayName("[Exception] 잘못된 DataSource 정보면 DB 커넥션을 획득하는데 실패한다")
        void test() {
            // given
            DataSourceProvider dataSourceProvider = new DataSourceProvider(
                    "이상한JdbcURL", "이상한사용자이름", "이상한비밀번호");
            DatabaseConnector databaseConnector = new DatabaseConnector(dataSourceProvider);
            ArticleDao articleDao = new ArticleDao(databaseConnector);
            article1.setPrimaryKey(1L);

            // when then
            assertThatThrownBy(() -> articleDao.insert(article1)).isInstanceOf(InvalidDataSourceException.class);
            assertThatThrownBy(() -> articleDao.selectById(article1.getId())).isInstanceOf(
                    InvalidDataSourceException.class);
            assertThatThrownBy(() -> articleDao.deleteById(article1.getId())).isInstanceOf(
                    InvalidDataSourceException.class);
        }
    }
}