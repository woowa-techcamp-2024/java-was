package codesquad.servlet.database.connection;

import codesquad.domain.UserStorage;
import codesquad.domain.model.User;
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

public class UserDao implements UserStorage {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    private final DatabaseConnector databaseConnector;

    public UserDao(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public Long insert(User user) {
        String sql = "INSERT INTO users (user_id, password, name, email) VALUES (?, ?, ?, ?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());

            int rowCounts = preparedStatement.executeUpdate();
            if (rowCounts == 0) {
                throw new InvalidDataAccessException("Failed to insert user into database. No rows affected");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long generatedKey = generatedKeys.getLong(1);
                    user.setPrimaryKey(generatedKey);
                    return generatedKey;
                } else {
                    throw new InvalidDataAccessException("Failed to generate primary key for user.");
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
    public Optional<User> selectById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = userMapper(resultSet);
                    return Optional.of(user);
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            logger.debug("[SQL Select Exception]", e);
            throw new InvalidDataSourceException(e);
        }
    }

    @Override
    public Optional<User> selectByUserId(String userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = userMapper(resultSet);
                    return Optional.of(user);
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            logger.debug("[SQL Select Exception]", e);
            throw new InvalidDataSourceException(e);
        }
    }

    private User userMapper(ResultSet resultSet) throws SQLException {
        User user = new User(
                resultSet.getString("user_id"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("email"));
        user.setPrimaryKey(resultSet.getLong("id"));
        return user;
    }

    @Override
    public List<User> selectAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(userMapper(resultSet));
                }
            }

        } catch (SQLException e) {
            logger.debug("[Database Connection Exception]", e);
            throw new InvalidDataSourceException(e);
        }

        return users;
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            return;
        }

        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            int rowCounts = preparedStatement.executeUpdate();
            if (rowCounts == 0) {
                throw new InvalidDataAccessException("Failed to delete user from database. No rows affected");
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
