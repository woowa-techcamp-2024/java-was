package codesquad.servlet.handler.api;

import codesquad.domain.model.Comment;
import codesquad.domain.model.User;

public class CommentResponse {

    private final Long id;
    private final String commenterName;
    private final String content;

    public CommentResponse(Long id, String commenterName, String content) {
        this.id = id;
        this.commenterName = commenterName;
        this.content = content;
    }

    public static CommentResponse of(Comment comment, User commenter) {
        return new CommentResponse(comment.getId(), commenter.getName(), comment.getContent());
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\" : \"" + id + "\"," +
                "\"commenterName\" : \"" + commenterName + "\"," +
                "\"content\" : \"" + content + "\"" +
                "}";
    }

}
