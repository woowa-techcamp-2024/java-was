package codesquad.domain.db;

import codesquad.domain.DatabaseSource;
import codesquad.domain.entity.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class PostDatabase implements Database<Integer, Post> {

    @Override
    public Integer append(Post data) {
        Integer id = null;
        try (Connection connection = DatabaseSource.connect()){
            String createPostSQL = "INSERT INTO posts (userid, title, contents, image) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(createPostSQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, data.userid());
            pstmt.setString(2, data.title());
            pstmt.setString(3, data.contents());
            pstmt.setString(4, data.image());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) id = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public Post getById(Integer id) {
        Post post = null;
        try (Connection connection = DatabaseSource.connect()) {
            String getPostSQL = "SELECT * FROM posts WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(getPostSQL);
            pstmt.setString(1, id.toString());

            var resultSet = pstmt.executeQuery();
            if (!resultSet.next()) return null;
            post = new Post(resultSet.getString("userid"), resultSet.getString("title"), resultSet.getString("contents"), resultSet.getString("image"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public Map<Integer, Post> getAll() {
        Map<Integer, Post> result = new HashMap<>();
        try (Connection connection = DatabaseSource.connect()){
            String getAllPostsSQL = "SELECT * FROM posts";
            var resultSet = connection.prepareStatement(getAllPostsSQL).executeQuery();
            while (resultSet.next()) {
                result.put(resultSet.getInt("id"), new Post(resultSet.getString("userid"), resultSet.getString("title"), resultSet.getString("contents"), resultSet.getString("image")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void deleteById(Integer id) {
        try (Connection connection = DatabaseSource.connect()) {
            String deletePostSQL = "DELETE FROM posts WHERE id = %s".formatted(id);
            connection.prepareStatement(deletePostSQL).executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
