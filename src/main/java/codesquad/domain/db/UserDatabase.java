package codesquad.domain.db;

import codesquad.domain.DatabaseSource;
import codesquad.domain.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserDatabase implements Database<Integer, User> {

    public User selectByUserId(String userid) {
        User user = null;
        try (Connection connection = DatabaseSource.connect()) {
            String getUserSQL = "SELECT * FROM users WHERE userid = ?";
            PreparedStatement pstmt = connection.prepareStatement(getUserSQL);
            pstmt.setString(1, userid);
            var resultSet = pstmt.executeQuery();
            if (!resultSet.next()) return null;
            user = new User(resultSet.getString("userid"), resultSet.getString("nickname"), resultSet.getString("password"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
