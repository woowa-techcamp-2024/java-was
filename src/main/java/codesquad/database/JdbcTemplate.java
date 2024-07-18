package codesquad.database;

import org.h2.jdbcx.JdbcConnectionPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class JdbcTemplate {

    private static JdbcTemplate instance;

    private final DataSource dataSource;

    private JdbcTemplate(H2Config h2Config) {
        dataSource = JdbcConnectionPool.create(h2Config.jdbcUrl(), h2Config.username(), h2Config.password());
    }

    public static JdbcTemplate getInstance(H2Config h2Config) {
        if (instance == null) {
            instance = new JdbcTemplate(h2Config);
        }
        return instance;
    }

    public void update(String sql, Object... arguments) {
        execute(statement -> {
            try {
                return statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, sql, arguments);
    }

    public <T> T queryForObject(String sql, Class<T> clazz, Object... arguments) {
        return execute(statement -> {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return RowMapper.mapRow(resultSet, clazz);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, sql, arguments);
    }

    public <T> List<T> queryForList(String sql, Class<T> clazz, Object... arguments) {
        return execute(statement -> {
            try (ResultSet resultSet = statement.executeQuery()) {
                return RowMapper.mapRows(resultSet, clazz);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, sql, arguments);
    }

    private <T> T execute(Function<PreparedStatement, T> function, String sql, Object... arguments) {
        if (Objects.isNull(sql) || sql.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] SQL 문이 비어있습니다.");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < arguments.length; i++) {
                preparedStatement.setObject(i + 1, arguments[i]);
            }
            return function.apply(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
