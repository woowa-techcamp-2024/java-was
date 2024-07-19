package codesquad.servlet.database.connection;

import codesquad.domain.CommentStorage;
import codesquad.domain.model.Comment;
import codesquad.servlet.database.exception.InvalidDataAccessException;
import codesquad.servlet.database.exception.InvalidDataSourceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommentDao implements CommentStorage {

    private static final Logger logger = LoggerFactory.getLogger(CommentDao.class);

    private final DatabaseConnector databaseConnector;

    public CommentDao(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public Long insert(Comment comment) {
        String sql = "INSERT INTO comment (commenter_id, article_id, content) VALUES (?, ?, ?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, comment.getCommenterId());
            preparedStatement.setLong(2, comment.getArticleId());
            preparedStatement.setString(3, comment.getContent());

            int rowCounts = preparedStatement.executeUpdate();
            if (rowCounts == 0) {
                throw new InvalidDataAccessException("Failed to insert comment into database. No rows affected");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long generatedKey = generatedKeys.getLong(1);
                    comment.setPrimaryKey(generatedKey);
                    return generatedKey;
                } else {
                    throw new InvalidDataAccessException("Failed to generate primary key for comment.");
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
    public List<Comment> selectByArticleId(Long articleId) {
        String sql = "SELECT * FROM comment WHERE article_id = ?";

        List<Comment> comments = new ArrayList<>();
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, articleId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Comment comment = commentMapper(articleId, resultSet);
                    comments.add(comment);
                }
            }
        } catch (SQLException e) {
            logger.debug("[SQL Select Exception]", e);
            throw new InvalidDataSourceException(e);
        }

        return comments;
    }

    private Comment commentMapper(Long articleId, ResultSet resultSet) throws SQLException {
        Long commenterId = resultSet.getLong("commenter_id");
        String content = resultSet.getString("content");
        Comment comment = new Comment(commenterId, articleId, content);
        comment.setPrimaryKey(resultSet.getLong("id"));
        return comment;
    }
}
