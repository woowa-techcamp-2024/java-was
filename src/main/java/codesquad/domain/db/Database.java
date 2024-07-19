package codesquad.domain.db;

import codesquad.domain.DatabaseSource;
import codesquad.domain.sql.SQLExecutor;
import codesquad.domain.sql.SQLGenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public interface Database<K, V> {
    SQLGenerator sqlGenerator = SQLGenerator.getInstance();
    SQLExecutor sqlExecutor = SQLExecutor.getInstance();

    default K insert(V data) {
        String className = findClassName();
        Field[] fields = findFields();
        for (Field field : fields) field.setAccessible(true);

        K id = null;
        try (Connection connection = DatabaseSource.connect()) {
            String insertSQL = sqlGenerator.generateInsertSQL(className, fields);
            ResultSet rs = sqlExecutor.executeInsert(connection, insertSQL, fields, data);
            if (rs.next()) id = (K) rs.getBytes(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    default V selectById(K id) {
        String className = findClassName();
        Field[] fields = findFields();
        Constructor<V> constructor = (Constructor<V>) findConstructor();

        V data = null;
        try (Connection connection = DatabaseSource.connect()) {
            String selectSQL = sqlGenerator.generateSelectByIdSQL(className);
            ResultSet rs = sqlExecutor.executeSelectById(connection, selectSQL, id);

            if (!rs.next()) return null;
            Object[] objects = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) objects[i] = rs.getString(fields[i].getName());
            data = constructor.newInstance(objects);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    default Map<K, V> selectAll() {
        String className = findClassName();
        Field[] fields = findFields();
        Constructor<V> constructor = (Constructor<V>) findConstructor();

        Map<K, V> result = new HashMap<>();
        try (Connection connection = DatabaseSource.connect()) {
            String selectAllSQL = sqlGenerator.generateSelectAllSQL(className);
            ResultSet rs = sqlExecutor.executeSelectAll(connection, selectAllSQL);

            while (rs.next()) {
                Object[] objects = new Object[fields.length];
                for (int i = 0; i < fields.length; i++) objects[i] = rs.getString(fields[i].getName());
                V data = constructor.newInstance(objects);
                result.put((K) rs.getBytes(1), data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    default void deleteById(K id) {}

    private Field[] findFields() {
        Class<?> clazz = (Class<?>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[1];
        return clazz.getDeclaredFields();
    }

    private String findClassName() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericInterfaces()[0];
        Class<?> clazz = (Class<?>) type.getActualTypeArguments()[1];
        return clazz.getSimpleName().toLowerCase();
    }


    private Constructor<?> findConstructor() {
        try {
            Class<?> clazz = (Class<?>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[1];
            return clazz.getDeclaredConstructors()[0];
        } catch (Exception e) {
            return null;
        }
    }
}
