package codesquad.webserver.db.article;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.annotation.Component;
import codesquad.webserver.db.user.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleRepository implements ArticleDatabase {

    private static final Logger logger = LoggerFactory.getLogger(ArticleRepository.class);
    private final DataSource dataSource;

    @Autowired
    public ArticleRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        initializeDatabase();
    }

    private void initializeDatabase() {
        String createArticlesTable = "CREATE TABLE IF NOT EXISTS `articles` (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "title VARCHAR(255) NOT NULL," +
                "content TEXT NOT NULL," +
                "user_id VARCHAR(255) NOT NULL" +
                ")";
        String createImagesTable = "CREATE TABLE IF NOT EXISTS `images` (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "path VARCHAR(255) NOT NULL," +
                "filename VARCHAR(255) NOT NULL," +
                "article_id BIGINT NOT NULL" +
                ")";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createArticlesTable);
            stmt.execute(createImagesTable);
            logger.info("Database tables initialized successfully");
        } catch (SQLException e) {
            logger.error("Failed to initialize database tables", e);
            throw new RuntimeException("Failed to initialize database tables", e);
        }
    }

    @Override
    public Article save(Article article) {
        String insertArticleSql = "INSERT INTO `articles` (title, content, user_id) VALUES (?, ?, ?)";
        String insertImageSql = "INSERT INTO `images` (path, filename, article_id) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            Long articleId;
            try (PreparedStatement pstmt = conn.prepareStatement(insertArticleSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, article.getTitle());
                pstmt.setString(2, article.getContent());
                pstmt.setString(3, article.getAuthor().getUserId());
                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        articleId = generatedKeys.getLong(1);
                    } else {
                        throw new SQLException("Failed to create article, no ID obtained.");
                    }
                }
            }

            Image savedImage = null;
            if (article.getImage() != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(insertImageSql, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, article.getImage().getPath());
                    pstmt.setString(2, article.getImage().getFilename());
                    pstmt.setLong(3, articleId);
                    pstmt.executeUpdate();

                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            savedImage = new Image(generatedKeys.getLong(1), article.getImage().getPath(),
                                    article.getImage().getFilename(), articleId);
                        }
                    }
                }
            }

            conn.commit();
            return new Article(articleId, article.getTitle(), article.getContent(), article.getAuthor(), savedImage);
        } catch (SQLException e) {
            logger.error("Failed to save article", e);
            throw new RuntimeException("Failed to save article", e);
        }
    }

    @Override
    public Optional<Article> findByArticleId(long id) {
        String sql = "SELECT a.id as article_id, a.title, a.content, a.user_id, " +
                "u.name as user_name, u.password as user_password, " +
                "i.id as image_id, i.path, i.filename " +
                "FROM articles a " +
                "JOIN users u ON a.user_id = u.id " +
                "LEFT JOIN images i ON a.id = i.article_id " +
                "WHERE a.id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User author = new User(rs.getString("user_id"), rs.getString("user_password"),
                            rs.getString("user_name"));
                    Image image = null;
                    long imageId = rs.getLong("image_id");
                    if (!rs.wasNull()) {
                        image = new Image(imageId, rs.getString("path"), rs.getString("filename"), id);
                    }
                    return Optional.of(
                            new Article(rs.getLong("article_id"), rs.getString("title"), rs.getString("content"),
                                    author, image));
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to find article with id {}", id, e);
            throw new RuntimeException("Failed to find article", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Article> findAllArticle(int page, int pageSize) {
        String sql = "SELECT a.id as article_id, a.title, a.content, a.user_id, " +
                "u.name as user_name, u.password as user_password, " +
                "i.id as image_id, i.path, i.filename " +
                "FROM articles a " +
                "JOIN users u ON a.user_id = u.id " +
                "LEFT JOIN images i ON a.id = i.article_id " +
                "ORDER BY a.id DESC " +
                "LIMIT ? OFFSET ?";

        List<Article> articles = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, (page - 1) * pageSize);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User author = new User(rs.getString("user_id"), rs.getString("user_password"),
                            rs.getString("user_name"));
                    Image image = null;
                    long imageId = rs.getLong("image_id");
                    if (!rs.wasNull()) {
                        image = new Image(imageId, rs.getString("path"), rs.getString("filename"),
                                rs.getLong("article_id"));
                    }
                    Article article = new Article(rs.getLong("article_id"), rs.getString("title"),
                            rs.getString("content"), author, image);
                    articles.add(article);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding articles with pagination", e);
            throw new RuntimeException("Error finding articles with pagination", e);
        }

        return articles;
    }

    @Override
    public int getTotalArticleCount() {
        String sql = "SELECT COUNT(*) FROM articles";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Error getting total article count", e);
            throw new RuntimeException("Error getting total article count", e);
        }

        return 0;
    }

    @Override
    public void clear() {
        String clearImagesSql = "DELETE FROM images";
        String clearArticlesSql = "DELETE FROM articles";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try (Statement stmt = conn.createStatement()) {
                int imagesDeleted = stmt.executeUpdate(clearImagesSql);
                logger.info("Cleared {} rows from images table", imagesDeleted);

                int articlesDeleted = stmt.executeUpdate(clearArticlesSql);
                logger.info("Cleared {} rows from articles table", articlesDeleted);

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                logger.error("Error clearing database", e);
                throw new RuntimeException("Error clearing database", e);
            }
        } catch (SQLException e) {
            logger.error("Error getting database connection", e);
            throw new RuntimeException("Error getting database connection", e);
        }
    }
}