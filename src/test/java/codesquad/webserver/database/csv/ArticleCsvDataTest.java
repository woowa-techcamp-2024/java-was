package codesquad.webserver.database.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;

import codesquad.application.domain.Article;
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
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleCsvDataTest {
    private Logger log = LoggerFactory.getLogger(ArticleCsvDataTest.class);

    @Test
    public void articleData(){
        try{
            Class.forName("codesquad.webserver.database.csv.CsvJdbcDriver");
            try(Connection con = CsvConnectionManager.getConnection()) {
                try(Statement stmt = con.createStatement()){
                    String query;
                    query = "DROP TABLE IF EXISTS ARTICLES";
                    stmt.execute(query);
                    log.info("user table has benn dropped");
                    query = "CREATE TABLE IF NOT EXISTS ARTICLES (" +
                            "ID," +
                            "TITLE," +
                            "CONTENT," +
                            "AUTHOR," +
                            "IMAGE_PATH,"+
                            "CREATED_DT" +
                            ")";
                    stmt.execute(query);
                    log.info("Articles table created or already exists.");
                    insert();
                    getAll();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }catch(ClassNotFoundException e){

        }
    }

    private void insert(){
        String key = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        String content = "안녕하세요\n무튼첫글입니다.";
        Article insertArticle = new Article(key, "a", content, "c", "./upload/81a52b74-1848-408d-a9a5-15b2d197c16c.png", now);
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
            assertEquals(1, affectedRows);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void getAll(){
        List<Article> articles = new ArrayList<>();
        try(Connection con = CsvConnectionManager.getConnection(); Statement stmt = con.createStatement()){
            String query = "SELECT * FROM ARTICLES";            ResultSet rs = stmt.executeQuery(query);
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
            assertEquals(1, articles.size());
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }
}
