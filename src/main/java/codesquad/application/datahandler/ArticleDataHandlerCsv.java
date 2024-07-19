package codesquad.application.datahandler;

import codesquad.application.domain.Article;
import codesquad.webserver.annotation.DataHandler;
import codesquad.webserver.database.csv.CsvConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DataHandler("ArticleDataHandlerCsv")
public class ArticleDataHandlerCsv implements ArticleDataHandler{
    private static final Logger log = LoggerFactory.getLogger(ArticleDataHandlerCsv.class);

    static {
        try {
            Class.forName("codesquad.webserver.database.csv.CsvJdbcDriver");

            try (Connection con = CsvConnectionManager.getConnection()) {
                try (Statement stmt = con.createStatement()) {
                    String query;
                    query = "DROP TABLE IF EXISTS ARTICLES";
                    stmt.execute(query);
                    log.info("user table has benn dropped");
                    query = "CREATE TABLE IF NOT EXISTS ARTICLES (" +
                            "ID," +
                            "TITLE," +
                            "CONTENT," +
                            "AUTHOR," +
                            "IMAGE_PATH," +
                            "CREATED_DT" +
                            ")";
                    stmt.execute(query);
                    log.info("Articles table created or already exists.");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void insert(Article article) {
        String key = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        Article insertArticle = new Article(key, article.getTitle(), article.getContent(), article.getAuthor(), article.getImagePath(), now);
        String sql = "INSERT INTO articles (id, title, content, author, image_path, created_dt) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = CsvConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
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
        try(Connection con = CsvConnectionManager.getConnection(); Statement stmt = con.createStatement()){
            String query = "SELECT * FROM ARTICLES";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                String id = rs.getString("ID");
                String title = rs.getString("TITLE");
                String content = rs.getString("CONTENT");
                String author = rs.getString("AUTHOR");
                String imagePath = rs.getString("IMAGE_PATH");
                LocalDateTime createdDt = rs.getTimestamp("CREATED_DT").toLocalDateTime();
                log.info("article {} {} {} {} {} {}", id, title, content, author, imagePath, createdDt);
                articles.add(new Article(id, title, content, author, imagePath, createdDt));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return articles;
    }

    @Override
    public void deleteAll() {

    }
}
