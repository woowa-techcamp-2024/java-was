package codesquad.webserver.db.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvStatement implements Statement {

    private static final Logger logger = LoggerFactory.getLogger(CsvStatement.class);
    protected final CsvConnection connection;
    protected ResultSet lastResultSet;

    public CsvStatement(CsvConnection connection) {
        this.connection = connection;
    }


    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        if (sql.trim().toLowerCase().startsWith("select")) {
            return executeSelect(sql);
        }
        throw new SQLException("Only select statements are supported");
    }

    private ResultSet executeSelect(String sql) throws SQLException {
        List<String[]> data = loadAllData();
        lastResultSet = new CsvResultSet(data);
        return lastResultSet;
    }

    private List<String[]> loadAllData() throws SQLException {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(connection.getCsvFilePath()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line.split(","));
            }
        } catch (IOException e) {
            throw new SQLException("Error reading CSV");
        }
        return data;
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        if (sql.trim().toLowerCase().startsWith("insert")) {
            return executeInsert(sql);
        }
        if (sql.trim().toLowerCase().startsWith("delete")) {
            return executeDelete(sql);
        }
        throw new SQLException("Only insert statements are supported");
    }

    public int executeDelete(String sql) throws SQLException {
        File csvFile = new File(connection.getCsvFilePath());
        File tempFile = new File(csvFile.getParent(), csvFile.getName() + ".tmp");
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile));
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            String header = reader.readLine();
            if (header != null) {
                writer.println(header);  // 헤더를 다시 씁니다.
            }

            if (!tempFile.renameTo(csvFile)) {
                throw new IOException("Could not rename temp file to " + csvFile.getAbsolutePath());
            }

            return 1;  // 또는 실제로 삭제된 행의 수를 반환
        } catch (IOException e) {
            throw new SQLException("Error deleting from csv", e);
        }
    }

    private int executeInsert(String sql) throws SQLException {
        String[] values = sql.substring(sql.indexOf("VALUES") + 7).replace("(", "").replace(")", "").split(",");
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].trim();
        }
        String filePath = connection.getCsvFilePath();
        logger.info("Attempting to write to file: {}", filePath);
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(String.join(",", values).trim() + "\n");
            return 1; // 1 row affected
        } catch (IOException e) {
            logger.error("Error writing to CSV file: {}", filePath, e);
            throw new SQLException("Error writing to CSV file", e);
        }
    }

    @Override
    public void close() throws SQLException {
        if (lastResultSet != null) {
            lastResultSet.close();
        }
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return 0;
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {

    }

    @Override
    public int getMaxRows() throws SQLException {
        return 0;
    }

    @Override
    public void setMaxRows(int max) throws SQLException {

    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {

    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return 0;
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {

    }

    @Override
    public void cancel() throws SQLException {

    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public void setCursorName(String name) throws SQLException {

    }

    @Override
    public boolean execute(String sql) throws SQLException {
        return false;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return null;
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return 0;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return false;
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {

    }

    @Override
    public int getFetchDirection() throws SQLException {
        return 0;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {

    }

    @Override
    public int getFetchSize() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetType() throws SQLException {
        return 0;
    }

    @Override
    public void addBatch(String sql) throws SQLException {

    }

    @Override
    public void clearBatch() throws SQLException {

    }

    @Override
    public int[] executeBatch() throws SQLException {
        return new int[0];
    }

    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return false;
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return null;
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return 0;
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return false;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return 0;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {

    }

    @Override
    public boolean isPoolable() throws SQLException {
        return false;
    }

    @Override
    public void closeOnCompletion() throws SQLException {

    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
