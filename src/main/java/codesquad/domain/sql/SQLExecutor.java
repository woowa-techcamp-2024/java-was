package codesquad.domain.sql;

import codesquad.domain.DatabaseSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Objects;

public class SQLExecutor {
    private static final Logger logger = LoggerFactory.getLogger(SQLExecutor.class);
    private static final SQLExecutor sqlExecutor = new SQLExecutor();

    private SQLExecutor() {}
    public static SQLExecutor getInstance() {
        return sqlExecutor;
    }

    public ResultSet executeInsert(Connection connection, String sql, Field[] fields, Object data) throws SQLException, IllegalAccessException {
        PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < fields.length; i++) {
            pstmt.setString(i + 1, fields[i].get(data).toString());
        }
        pstmt.executeUpdate();
        return pstmt.getGeneratedKeys();
    }

    public <K> ResultSet executeSelectById(Connection connection, String sql, K id) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, id.toString());
        return pstmt.executeQuery();
    }

    public ResultSet executeSelectAll(Connection connection, String sql) throws SQLException {
        return connection.prepareStatement(sql).executeQuery();
    }

    public static void executeSQLFile(Connection connection, String path) {
        try (Statement statement = connection.createStatement()) {
            InputStream inputStream = DatabaseSource.class.getResourceAsStream(path);
            String[] sqls = new String(Objects.requireNonNull(inputStream).readAllBytes()).split("\n");
            for (String sql: sqls) {
                statement.execute(sql);
                logger.debug("SQL: {}", sql);
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
