package codesquad.domain;

import codesquad.domain.model.Comment;
import java.util.List;

public interface CommentStorage {

    Long insert(Comment comment);

    List<Comment> selectByArticleId(Long articleId);

}
