package codesquad.database;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class RowMapper {

    private RowMapper() {
    }

    public static <T> T mapRow(ResultSet resultSet, Class<T> clazz) throws SQLException {
        try {
            Object[] arguments = extractFieldValues(resultSet, clazz);
            Constructor<T> constructor = findSuitableConstructor(clazz, arguments);
            return constructor.newInstance(arguments);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> mapRows(ResultSet resultSet, Class<T> clazz) throws SQLException {
        List<T> results = new ArrayList<>();
        while (resultSet.next()) {
            T mapped = mapRow(resultSet, clazz);
            results.add(mapped);
        }
        return results;
    }

    private static <T> Constructor<T> findSuitableConstructor(Class<T> clazz, Object[] arguments) throws NoSuchMethodException {
        return clazz.getDeclaredConstructor(
                Arrays.stream(arguments)
                        .map(Object::getClass)
                        .toArray(Class[]::new)
        );
    }

    private static Object[] extractFieldValues(ResultSet resultSet, Class<?> clazz) throws SQLException, IllegalArgumentException {
        Field[] fields = clazz.getDeclaredFields();
        Object[] values = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            Class<?> fieldType = fields[i].getType();
            values[i] = getValueFromResultSet(resultSet, fieldName, fieldType);
        }
        return values;
    }

    private static Object getValueFromResultSet(ResultSet resultSet, String fieldName, Class<?> fieldType) throws SQLException {
        if (fieldType == int.class || fieldType == Integer.class) {
            return resultSet.getInt(fieldName);
        } else if (fieldType == long.class || fieldType == Long.class) {
            return resultSet.getLong(fieldName);
        } else if (fieldType == double.class || fieldType == Double.class) {
            return resultSet.getDouble(fieldName);
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return resultSet.getBoolean(fieldName);
        } else if (fieldType == String.class) {
            return resultSet.getString(fieldName);
        }
        return resultSet.getObject(fieldName);
    }
}
