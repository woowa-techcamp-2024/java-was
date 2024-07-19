package codesquad.database;

import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private JdbcTemplate() {
        String url = H2Properties.getUrl();
        String username = H2Properties.getUsername();
        String password = H2Properties.getPassword();
        dataSource = JdbcConnectionPool.create(url, username, password);
    }

    public static JdbcTemplate getInstance() {
        if (instance == null) {
            instance = new JdbcTemplate();
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
