package codesquad.webserver.db.article;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.annotation.Component;
import codesquad.webserver.db.csv.CsvDatabaseInitializer;
import codesquad.webserver.db.user.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CsvArticleRepository implements ArticleDatabase {
    private static final Logger logger = LoggerFactory.getLogger(CsvArticleRepository.class);
    private static final String CSV_FILE_NAME = "articles.csv";
    private static final String CSV_HEADER = "id,title,content,user_id,user_name,image_path,image_filename";
    private static final Map<String, Integer> COLUMN_INDICES;
    private final String jdbcUrl;
    private AtomicLong articleId = new AtomicLong(1);

    static {
        String[] headers = CSV_HEADER.split(",");
        COLUMN_INDICES = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            COLUMN_INDICES.put(headers[i], i);
        }
        try {
            Class.forName("codesquad.webserver.db.csv.CsvDriver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Failed to load CSV JDBC driver: " + e.getMessage());
        }
    }

    public int getColumnIndex(String columnName) {
        Integer index = COLUMN_INDICES.get(columnName);
        if (index == null) {
            throw new IllegalArgumentException("Column not found: " + columnName);
        }
        return index + 1;
    }

    @Autowired
    public CsvArticleRepository() {
        try {
            String filePath = CsvDatabaseInitializer.initializeDatabase(CSV_FILE_NAME, CSV_HEADER);
            this.jdbcUrl = CsvDatabaseInitializer.getJdbcUrl(filePath);
            logger.info("JDBC URL for articles: {}", jdbcUrl);

        } catch (IOException e) {
            logger.error("Failed to initialize article repository", e);
            throw new RuntimeException("Failed to initialize article repository", e);
        }
    }

    @Override
    public Article save(Article article) {
        String sql = "INSERT INTO articles (id, title, content, user_id, user_name, image_path, image_filename) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            long id = articleId.getAndIncrement();

            pstmt.setString(1, String.valueOf(id));
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getContent());
            pstmt.setString(4, article.getAuthor().getUserId());
            pstmt.setString(5, article.getAuthor().getName());
            if (article.getImage() != null) {
                pstmt.setString(6, article.getImage().getPath());
                pstmt.setString(7, article.getImage().getFilename());
            } else {
                pstmt.setString(6, null);
                pstmt.setString(7, null);
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating article failed, no rows affected.");
            }

            return new Article(id, article.getTitle(), article.getContent(), article.getAuthor(),
                    article.getImage());

        } catch (SQLException e) {
            logger.error("Failed to save article", e);
            throw new RuntimeException("Failed to save article", e);
        }
    }

    @Override
    public Optional<Article> findByArticleId(long id) {
        String sql = "SELECT * FROM articles WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createArticleFromResultSet(rs));
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
        String sql = "SELECT * FROM articles";
        List<Article> articles = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    articles.add(createArticleFromResultSet(rs));
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
        String sql = "SELECT * FROM articles";
        int count = 0;
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                count++;
            }
        } catch (SQLException e) {
            logger.error("Error getting total article count", e);
            throw new RuntimeException("Error getting total article count", e);
        }
        return count;
    }

    @Override
    public void clear() {
        String sql = "DELETE FROM articles";
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int rowsAffected = pstmt.executeUpdate();
            logger.info("Cleared {} rows from articles table", rowsAffected);
        } catch (SQLException e) {
            logger.error("Error clearing articles", e);
            throw new RuntimeException("Error clearing articles", e);
        }
    }

    private Article createArticleFromResultSet(ResultSet rs) throws SQLException {
        long id = rs.getLong(getColumnIndex("id"));
        String title = rs.getString(getColumnIndex("title"));
        String content = rs.getString(getColumnIndex("content"));
        String userId = rs.getString(getColumnIndex("user_id"));
        String username = rs.getString(getColumnIndex("user_name"));
        String imagePath = rs.getString(getColumnIndex("image_path"));
        String imageFilename = rs.getString(getColumnIndex("image_filename"));

        User author = new User(userId, null, username);
        Image image = (imagePath != null && imageFilename != null) ? new Image(0L, imagePath, imageFilename, id) : null;

        return new Article(id, title, content, author, image);
    }
}