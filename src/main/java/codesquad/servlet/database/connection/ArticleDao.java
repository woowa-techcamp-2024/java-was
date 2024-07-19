package codesquad.servlet.database.connection;

import codesquad.domain.ArticleStorage;
import codesquad.domain.model.Article;
import codesquad.servlet.database.exception.InvalidDataAccessException;
import codesquad.servlet.database.exception.InvalidDataSourceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleDao implements ArticleStorage {

    private static final Logger logger = LoggerFactory.getLogger(ArticleDao.class);

    private final DatabaseConnector databaseConnector;

    public ArticleDao(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public Long insert(Article article) {
        String sql = "INSERT INTO article (author_id, title, content, image_path) VALUES (?, ?, ?, ?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, article.getAuthorId());
            preparedStatement.setString(2, article.getTitle());
            preparedStatement.setString(3, article.getContent());
            preparedStatement.setString(4, article.getImagePath());

            int rowCounts = preparedStatement.executeUpdate();
            if (rowCounts == 0) {
                throw new InvalidDataAccessException("Failed to insert article into database. No rows affected");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long generatedKey = generatedKeys.getLong(1);
                    article.setPrimaryKey(generatedKey);
                    return generatedKey;
                } else {
                    throw new InvalidDataAccessException("Failed to generate primary key for article.");
                }
            }

        } catch (InvalidDataAccessException e) {
            logger.debug("[SQL Insert Exception]", e);
            throw e;
        } catch (SQLException e) {
            logger.debug("[Database Connection Exception]", e);
            throw new InvalidDataSourceException(e);
        }
    }

    @Override
    public Optional<Article> selectById(Long id) {
        String sql = "SELECT * FROM article WHERE id = ?";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    Article article = articleMapper(resultSet);
                    return Optional.of(article);
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            logger.debug("[SQL Select Exception]", e);
            throw new InvalidDataSourceException(e);
        }
    }

    @Override
    public List<Article> selectAll() {
        String sql = "SELECT * FROM article";
        List<Article> articles = new ArrayList<>();

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    articles.add(articleMapper(resultSet));
                }
            }

        } catch (SQLException e) {
            logger.debug("[Database Connection Exception]", e);
            throw new InvalidDataSourceException(e);
        }

        return articles;
    }

    private Article articleMapper(ResultSet resultSet) throws SQLException {
        Long authorId = resultSet.getLong("author_id");
        String title = resultSet.getString("title");
        String content = resultSet.getString("content");
        String imagePath = resultSet.getString("image_path");
        Article article = new Article(authorId, title, content, imagePath);
        article.setPrimaryKey(resultSet.getLong("id"));

        return article;
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            return;
        }

        String sql = "DELETE FROM article WHERE id = ?";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            int rowCounts = preparedStatement.executeUpdate();
            if (rowCounts == 0) {
                throw new InvalidDataAccessException("Failed to delete article from database. No rows affected");
            }

        } catch (InvalidDataAccessException e) {
            logger.debug("[SQL Delete Exception]", e);
            throw e;
        } catch (SQLException e) {
            logger.debug("[Database Connection Exception]", e);
            throw new InvalidDataSourceException(e);
        }
    }
}
