package codesquad.domain.sql;

import java.lang.reflect.Field;

public class SQLGenerator {
    private static final SQLGenerator sqlGenerator = new SQLGenerator();

    private SQLGenerator() {}
    public static SQLGenerator getInstance() {
        return sqlGenerator;
    }

    public String generateInsertSQL(String className, Field[] fields) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO ").append(className).append("s (");

        for (int i = 0; i < fields.length; i++) {
            stringBuilder.append(fields[i].getName());
            if (i != fields.length - 1) stringBuilder.append(", ");
        }
        stringBuilder.append(") VALUES (");

        for (int i = 0; i < fields.length; i++) {
            stringBuilder.append("?");
            if (i != fields.length - 1) stringBuilder.append(", ");
        }
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    public String generateSelectByIdSQL(String className) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM ").append(className).append("s WHERE id = ?");
        return stringBuilder.toString();
    }

    public String generateSelectAllSQL(String className) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM ").append(className).append("s");
        return stringBuilder.toString();
    }
}
