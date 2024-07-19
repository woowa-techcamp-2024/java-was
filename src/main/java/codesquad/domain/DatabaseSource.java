package codesquad.domain;

import codesquad.domain.sql.SQLExecutor;
import codesquad.utils.FileUtil;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseSource {
    private static final String jdbcURL = "jdbc:h2:file:./was/db";
    private static final String username = "sa";
    private static final String password = "";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(jdbcURL, username, password);
    }

    public static void init() {
        try {
            FileUtil.createFile(System.getProperty("user.home") + File.separator +
                    "was" + File.separator + "db.mv.db");
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTable() throws SQLException {
        SQLExecutor.executeSQLFile(connect(), "/sql/drop.sql");
        SQLExecutor.executeSQLFile(connect(), "/sql/create.sql");
    }
}
