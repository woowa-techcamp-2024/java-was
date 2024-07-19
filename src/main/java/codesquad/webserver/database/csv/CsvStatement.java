package codesquad.webserver.database.csv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvStatement implements Statement {
    private CsvConnection connection;
    private List<String[]> data;
    private String csvDirectory;
    private final Logger log = LoggerFactory.getLogger(CsvStatement.class);

    public CsvStatement(CsvConnection connection) {
        this.connection = connection;
        this.csvDirectory = connection.getCsvFilePath();
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        sql = sql.trim().toLowerCase();
        if(!sql.toLowerCase().startsWith("select")){
            throw new SQLException("");
        }
        Pattern pattern = Pattern.compile("select\\s+(.+)\\s+from\\s+(\\w+)(?:\\s+where\\s+(.+))?");
        Matcher matcher = pattern.matcher(sql);

        if (!matcher.find()) {
            throw new SQLException("Invalid SELECT statement");
        }

        String columns = matcher.group(1);
        String tableName = matcher.group(2);
        String whereClause = matcher.group(3);

        loadCsvData(tableName);
        List<String[]> resultData = new ArrayList<>();

        // 컬럼 선택
        String[] selectedColumns = columns.equals("*") ? data.get(0) : columns.split(",");
        int[] columnIndices = getColumnIndices(data.get(0), selectedColumns);

        // 헤더 추가
        resultData.add(selectedColumns);

        // 데이터 필터링 및 선택
        for (int i = 1; i < data.size(); i++) {
            if (whereClause == null || evaluateWhereClause(data.get(0), data.get(i), whereClause)) {
                String[] row = new String[columnIndices.length];
                for (int j = 0; j < columnIndices.length; j++) {
                    row[j] = data.get(i)[columnIndices[j]];
                }
                resultData.add(row);
            }
        }

        return new CsvResultSet(resultData);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        String lowerSql = sql.toLowerCase();
        if(lowerSql.startsWith("insert")){
            return handleInsert(sql);
        }else if(lowerSql.startsWith("update")){
            return handleUpdate(sql);
        }else if(lowerSql.startsWith("delete")){
            return handleDelete(sql);
        }else{
            throw new SQLException("Unsupported SQL Operation");
        }
    }

    private void loadCsvData(String filaName) throws SQLException{
        data = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(connection.getCsvFilePath() + "/" + filaName + ".csv"))){
            String line;
            while((line = br.readLine()) != null){
                data.add(parseCsvLine(line));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveCsvData(String filename) throws SQLException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(connection.getCsvFilePath() + "/" + filename + ".csv"))) {
            for (String[] row : data) {
                bw.write(String.join(",", Arrays.stream(row).map(this::escapeNewlines).toArray(String[]::new)));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new SQLException("Error writing CSV file", e);
        }
    }

    private String[] parseCsvLine(String line) {
        String[] fields = line.split(",");
        for (int i = 0; i < fields.length; i++) {
            fields[i] = unescapeNewlines(fields[i]);
        }
        return fields;
    }

    private String unescapeNewlines(String value) {
        return value.replace("\\n", "\n").replace("\\r", "\r");
    }

    private int[] getColumnIndices(String[] headers, String[] selectedColumns) throws SQLException {
        int[] indices = new int[selectedColumns.length];
        for (int i = 0; i < selectedColumns.length; i++) {
            String columnName = selectedColumns[i].trim();
            if (columnName.contains(".")) {
                columnName = columnName.substring(columnName.indexOf(".") + 1);
            }
            indices[i] = getColumnIndex(headers, columnName);
            if (indices[i] == -1) {
                throw new SQLException("Column not found: " + selectedColumns[i]);
            }
        }
        return indices;
    }

    private int handleInsert(String sql) throws SQLException {
        // INSERT INTO table VALUES (val1, val2, ...)
        String regex = "insert\\s+into\\s+(\\w+)\\s*\\(([^)]*)\\)\\s*values\\s*\\((.+)\\)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(sql);

        if (!matcher.find()) {
            throw new SQLException("Invalid INSERT statement");
        }

        String tableName = matcher.group(1);
        String[] columns = matcher.group(2).split(",");
        String valuesGroup = matcher.group(3);

        // 값 파싱 (쌍따옴표 내의 쉼표와 개행은 무시)
        List<String> values = parseValues(valuesGroup);

        loadCsvData(tableName);

        String[] newRow = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            newRow[i] = escapeNewlines(values.get(i));
        }

        this.data.add(newRow);
        saveCsvData(tableName);
        log.info("insert: save csv");
        return 1;
    }

    private int handleUpdate(String sql) throws SQLException {
        // UPDATE table SET col1 = val1 WHERE col2 = val2
        Pattern pattern = Pattern.compile("update\\s+(\\w+)\\s+set\\s+(.+)\\s+where\\s+(.+)");
        Matcher matcher = pattern.matcher(sql);

        if (!matcher.find()) {
            throw new SQLException("Invalid UPDATE statement");
        }

        String tableName = matcher.group(1);
        String[] setClause = matcher.group(2).split(",");
        String whereClause = matcher.group(3);
        loadCsvData(tableName);

        int updatedRows = 0;

        Map<String, String> updates = new HashMap<>();
        for (String set : setClause) {
            String[] parts = set.trim().split("=");
            updates.put(parts[0].trim(), parts[1].trim().replaceAll("^'|'$", ""));
        }

        for (int i = 1; i < data.size(); i++) {
            if (evaluateWhereClause(data.get(0), data.get(i), whereClause)) {
                for (Map.Entry<String, String> entry : updates.entrySet()) {
                    int columnIndex = getColumnIndex(data.get(0), entry.getKey());
                    data.get(i)[columnIndex] = entry.getValue();
                }
                updatedRows++;
            }
        }

        saveCsvData(tableName);
        return updatedRows;
    }

    private int handleDelete(String sql) throws SQLException {
        // DELETE FROM table WHERE col = val
        Pattern pattern = Pattern.compile("delete\\s+from\\s+(\\w+)\\s+where\\s+(.+)");
        Matcher matcher = pattern.matcher(sql);
        if (!matcher.find()) {
            throw new SQLException("Invalid DELETE statement");
        }

        String tableName = matcher.group(1);
        String whereClause = matcher.group(2);
        List<String[]> newData = new ArrayList<>();
        loadCsvData(tableName);
        newData.add(data.get(0)); // 헤더 유지

        int deletedRows = 0;

        for (int i = 1; i < data.size(); i++) {
            if (!evaluateWhereClause(data.get(0), data.get(i), whereClause)) {
                newData.add(data.get(i));
            } else {
                deletedRows++;
            }
        }
        this.data = newData;
        saveCsvData(tableName);
        return deletedRows;
    }

    private String escapeNewlines(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\n", "\\n").replace("\r", "\\r");
    }

    private boolean evaluateWhereClause(String[] headers, String[] row, String whereClause) {
        //  column = value
        String[] parts = whereClause.split("=");
        if (parts.length != 2) return false;

        String column = parts[0].trim();
        String value = parts[1].trim();

        if (column.contains(".")) {
            column = column.substring(column.indexOf(".") + 1);
        }

        // 따옴표 제거 (있는 경우)
        value = value.replaceAll("^['\"]|['\"]$", "");

        int columnIndex = getColumnIndex(headers, column);
        return columnIndex != -1 && row[columnIndex].equalsIgnoreCase(value);
    }

    private int getColumnIndex(String[] headers, String columnName) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }

    private List<String> parseValues(String valuesGroup) {
        List<String> values = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (char c : valuesGroup.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        values.add(sb.toString().trim());
        return values;
    }



    @Override
    public boolean execute(String sql) throws SQLException {
        String lowerSql = sql.toLowerCase().trim();
        if (lowerSql.startsWith("create table")) {
            handleCreateTable(sql);
            return false; // DDL 명령은 false를 반환
        } else if (lowerSql.startsWith("drop table")) {
            handleDropTable(sql);
            return false; // DDL 명령은 false를 반환
        }
        return false;
    }

    private void handleCreateTable(String sql) throws SQLException {
        // CREATE TABLE IF NOT EXISTS table_name (column1, column2, ...)
        String[] parts = sql.split("\\s+", 7);
        if (parts.length < 7 || !parts[2].equalsIgnoreCase("IF") || !parts[3].equalsIgnoreCase("NOT") || !parts[4].equalsIgnoreCase("EXISTS")) {
            throw new SQLException("Invalid CREATE TABLE statement");
        }

        String tableName = parts[5];
        String columnDefinition = parts[6].replaceAll("[()]", "").trim();
        String[] columns = columnDefinition.split(",");
        for (int i = 0; i < columns.length; i++) {
            columns[i] = columns[i].trim().split("\\s+")[0]; // 컬럼 이름만 추출
        }

        File file = new File(csvDirectory, tableName + ".csv");
        if (file.exists()) {
            log.debug("Table already exists: " + tableName);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(String.join(",", columns));
            writer.newLine();
            log.debug("Table created: " + tableName);
        } catch (IOException e) {
            throw new SQLException("Error creating CSV file", e);
        }
    }
    private void handleDropTable(String sql) throws SQLException {
        // DROP TABLE IF EXISTS table_name
        String[] parts = sql.split("\\s+");
        if (parts.length != 5 || !parts[2].equalsIgnoreCase("IF") || !parts[3].equalsIgnoreCase("EXISTS")) {
            throw new SQLException("Invalid DROP TABLE statement");
        }

        String tableName = parts[4];
        File file = new File(csvDirectory, tableName + ".csv");

        if (!file.exists()) {
            log.debug("Table does not exist: " + tableName);
            return;
        }

        if (file.delete()) {
            log.debug("Table dropped: " + tableName);
        } else {
            throw new SQLException("Error dropping CSV file: " + tableName);
        }
    }

    @Override
    public void close() throws SQLException {

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
