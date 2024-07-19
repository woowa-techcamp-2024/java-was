package codesquad.database;

import codesquad.model.Article;

import java.util.List;

public final class ArticleRepository {

    private static ArticleRepository instance;

    private final JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();

    private ArticleRepository() {
    }

    public static ArticleRepository getInstance() {
        if (instance == null) {
            instance = new ArticleRepository();
        }
        return instance;
    }

    public void save(Article article) {
        String sql = "INSERT INTO article (content, imageName, userId) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, article.getContent(), article.getImageName(), article.getUserId());
    }

    public List<Article> findAll() {
        String sql = "SELECT * FROM article ORDER BY id DESC";
        return jdbcTemplate.queryForList(sql, Article.class);
    }
}
