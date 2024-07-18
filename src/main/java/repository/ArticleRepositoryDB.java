package repository;

import database.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import model.Article;
import org.slf4j.Logger;
import util.LoggerUtil;

public class ArticleRepositoryDB extends ArticleRepository {

    private static final Logger logger = LoggerUtil.getLogger();
    private static ArticleRepositoryDB instance;
    private Connection connection;

    private ArticleRepositoryDB() throws SQLException {
        this.connection = DatabaseConnector.getInstance().getConnection();
    }

    public static ArticleRepositoryDB getInstance() throws SQLException {
        if (instance == null) {
            instance = new ArticleRepositoryDB();
        }
        return instance;
    }

    @Override
    public void save(Article article) {
        String query = "INSERT INTO articles (title, content, user_id, photo_path) VALUES (?, ?, ?, ?)";
        logger.info("prepared query info: {}", query);
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, article.getTitle());
            statement.setString(2, article.getContent());
            statement.setString(3, article.getUserId());
            statement.setString(4, article.getPhotoPath());
            logger.info("final query info: {}", statement);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        article.setArticleId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error: {}", e.getMessage());
        }
    }

    @Override
    public Optional<Article> findById(Long id) {
        String query = "SELECT * FROM articles WHERE article_id = ?";
        Article article = null;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                article = new Article(resultSet.getString("title"),
                    resultSet.getString("content"),
                    resultSet.getString("user_id"),
                    resultSet.getString("photo_path"));
                article.setArticleId(resultSet.getLong("article_id"));
            }
        } catch (SQLException e) {
            logger.error("Error: {}", e.getMessage());
        }
        return Optional.ofNullable(article);
    }

    @Override
    public Map<Long, Article> getAllArticles() {
        String query = "SELECT * FROM articles";
        Map<Long, Article> articles = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Article article = new Article(resultSet.getString("title"),
                    resultSet.getString("content"),
                    resultSet.getString("user_id"),
                    resultSet.getString("photo_path"));
                article.setArticleId(resultSet.getLong("article_id"));
                articles.put(article.getArticleId(), article);
            }
        } catch (SQLException e) {
            logger.error("Error: {}", e.getMessage());
        }
        return articles;
    }
}
