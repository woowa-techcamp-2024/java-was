package codesquad.database;

import codesquad.model.Post;
import codesquad.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostH2Database {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final String JDBC_URL = "jdbc:h2:tcp://localhost/~/java-was";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    public void addPost(Post post) {
        String sql = "INSERT INTO POSTS (CONTENT, USER_ID) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, post.getContent());
            preparedStatement.setInt(2, post.getAuthor().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.info(e.toString());
            throw new RuntimeException("게시글 추가 실패", e);
        }
    }

    public List<Post> getAllPost() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM POSTS, USERS where POSTS.USER_ID = USERS.ID ORDER BY POSTS.ID DESC";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);

             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User(Integer.parseInt(resultSet.getString("ID")),
                        resultSet.getString("USER_ID"),
                        resultSet.getString("PASSWORD"),
                        resultSet.getString("NAME"));
                Post post = new Post(user, resultSet.getString("CONTENT"));
                posts.add(post);
            }
        } catch (SQLException e) {
            throw new RuntimeException("사용자 조회 실패", e);
        }
        return posts;
    }

}
