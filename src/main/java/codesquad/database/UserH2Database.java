package codesquad.database;

import codesquad.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserH2Database {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final String JDBC_URL = "jdbc:h2:tcp://localhost/~/java-was";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";


    public Optional<User> findUserById(String userId) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";

        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return Optional.of(new User(Integer.parseInt(resultSet.getString("ID")),
                        resultSet.getString("USER_ID"),
                        resultSet.getString("PASSWORD"),
                        resultSet.getString("NAME")));
            }
        } catch (SQLException e) {
            log.info(e.toString());
            throw new RuntimeException("사용자 조회 실패", e);
        }
        return Optional.empty();
    }


    public List<User> getAllUser() {

        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM USERS";

        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            while(resultSet.next()){
                User user = new User(Integer.parseInt(resultSet.getString("ID")),
                        resultSet.getString("USER_ID"),
                        resultSet.getString("PASSWORD"),
                        resultSet.getString("NAME"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("사용자 조회 실패", e);
        }
        return users;
    }

    public void addUser(User user) {
        String sql = "INSERT INTO USERS (USER_ID, PASSWORD, NAME) VALUES (?, ?, ?)";

        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows <= 0){
                throw new SQLException();
            }
        } catch (SQLException e) {
            log.info(e.toString());
            throw new RuntimeException("사용자 추가 실패", e);
        }
    }
}
