package codesquad.webserver.db.article;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import codesquad.webserver.db.user.User;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CsvArticleRepositoryTest {

    private CsvArticleRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new CsvArticleRepository();
    }

    @AfterEach
    public void tearDown() {
        repository.clear();
    }

    @Test
    @DisplayName("새로운 기사를 저장하고 올바르게 저장되었는지 확인한다")
    public void testSaveArticle() throws SQLException {
        Article article = new Article(0L, "Test Title", "Test Content", new User("user1", "password", "User Name"),
                new Image(0L, "path", "filename", 1L));

        Article savedArticle = repository.save(article);

        assertNotNull(savedArticle);
        assertEquals("Test Title", savedArticle.getTitle());
        assertEquals("Test Content", savedArticle.getContent());
        assertEquals("user1", savedArticle.getAuthor().getUserId());
    }

    @Test
    @DisplayName("특정 ID로 기사를 찾고 올바르게 반환되는지 확인한다")
    public void testFindByArticleId() throws SQLException {
        long articleId = 1L;
        Article article = new Article(articleId, "Test Title", "Test Content",
                new User("user1", "password", "User Name"), null);
        repository.save(article);

        Optional<Article> foundArticle = repository.findByArticleId(articleId);

        assertTrue(foundArticle.isPresent());
        assertEquals("Test Title", foundArticle.get().getTitle());
        assertEquals("Test Content", foundArticle.get().getContent());
        assertEquals("user1", foundArticle.get().getAuthor().getUserId());
    }

    @Test
    @DisplayName("모든 기사를 페이지네이션하여 반환하는 기능을 테스트한다")
    public void testFindAllArticles() {
        repository.clear();
        Article article1 = new Article(0L, "Title 1", "Content 1", new User("user1", "password", "User 1"), null);
        Article article2 = new Article(0L, "Title 2", "Content 2", new User("user2", "password", "User 2"), null);
        repository.save(article1);
        repository.save(article2);

        List<Article> articles = repository.findAllArticle(1, 10);

        assertNotNull(articles);
        assertEquals(2, articles.size());
        assertEquals("Title 1", articles.get(0).getTitle());
        assertEquals("Title 2", articles.get(1).getTitle());
    }

    @Test
    @DisplayName("총 기사 수를 반환하는 기능을 테스트한다")
    public void testGetTotalArticleCount() {
        repository.clear();
        Article article1 = new Article(0L, "Title 1", "Content 1", new User("user1", "password", "User 1"), null);
        Article article2 = new Article(0L, "Title 2", "Content 2", new User("user2", "password", "User 2"), null);
        repository.save(article1);
        repository.save(article2);

        int count = repository.getTotalArticleCount();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("모든 기사를 삭제하고 빈 리스트를 반환하는지 확인한다")
    public void testClear() {
        Article article1 = new Article(0L, "Title 1", "Content 1", new User("user1", "password", "User 1"), null);
        repository.save(article1);

        repository.clear();

        List<Article> articles = repository.findAllArticle(1, 10);
        assertTrue(articles.isEmpty());
    }
}
