package codesquad.application.datahandler;

import codesquad.application.domain.Article;
import codesquad.application.domain.User;
import codesquad.webserver.annotation.DataHandler;
import codesquad.webserver.database.DatabaseConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@DataHandler("ArticleDataHandlerJdbc")
public class ArticleDataHandlerJdbc implements ArticleDataHandler{
    private static final Logger log = LoggerFactory.getLogger(ArticleDataHandlerJdbc.class);

    static{
        try (Connection con = DatabaseConnectionManager.getConnection(); Statement stmt = con.createStatement()) {
            String query = "DROP TABLE IF EXISTS ARTICLES";
            stmt.execute(query);
            log.info("articles table has benn dropped");
            query = "CREATE TABLE IF NOT EXISTS ARTICLES (" +
                    "ID VARCHAR(36) PRIMARY KEY," +
                    "TITLE VARCHAR(255) NOT NULL," +
                    "CONTENT VARCHAR(255) NOT NULL," +
                    "AUTHOR VARCHAR(255) NOT NULL," +
                    "IMAGE_PATH VARCHAR(400) NOT NULL,"+
                    "CREATED_DT TIMESTAMP NOT NULL" +
                    ")";
            stmt.execute(query);
            log.info("Articles table created or already exists.");

            ResultSet rs = stmt.executeQuery("SHOW COLUMNS FROM ARTICLES");
            while (rs.next()) {
                log.info("Column: " + rs.getString("FIELD"));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void insert(Article article) {
        String key = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        Article insertArticle = new Article(key, article.getTitle(), article.getContent(), article.getAuthor(), article.getImagePath(), now);
        String sql = "INSERT INTO articles (id, title, content, author, image_path, created_dt) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, insertArticle.getId());
            pstmt.setString(2, insertArticle.getTitle());
            pstmt.setString(3, insertArticle.getContent());
            pstmt.setString(4, insertArticle.getAuthor());
            pstmt.setString(5, insertArticle.getImagePath());
            pstmt.setTimestamp(6, Timestamp.valueOf(insertArticle.getCreatedDt()));
            int affectedRows = pstmt.executeUpdate();
            log.info("Inserted article: " + insertArticle.toString() + ", Affected rows: " + affectedRows);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }

    }

    @Override
    public List<Article> getAll() {
        List<Article> articles = new ArrayList<>();
        try (Connection con = DatabaseConnectionManager.getConnection(); Statement stmt = con.createStatement()) {
            String query = "SELECT * FROM ARTICLES";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString("ID");
                String title = rs.getString("TITLE");
                String content = rs.getString("CONTENT");
                String author = rs.getString("AUTHOR");
                String imagePath = rs.getString("IMAGE_PATH");
                LocalDateTime createdDt = rs.getTimestamp("CREATED_DT").toLocalDateTime();
                articles.add(new Article(id, title, content, author, imagePath, createdDt));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return articles;
    }

    @Override
    public void deleteAll() {
        try (Connection con = DatabaseConnectionManager.getConnection(); Statement stmt = con.createStatement()) {
            String query = "DROP TABLE IF EXISTS ARTICLES";
            stmt.execute(query);
            log.info("articles table has benn dropped");
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }
}
