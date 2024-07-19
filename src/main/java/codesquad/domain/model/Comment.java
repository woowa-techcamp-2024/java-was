package codesquad.domain.model;

public class Comment {

    private Long id;
    private final Long commenterId;
    private final Long articleId;
    private final String content;

    public Comment(Long commenterId, Long articleId, String content) {
        this.commenterId = commenterId;
        this.articleId = articleId;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public Long getCommenterId() {
        return commenterId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public String getContent() {
        return content;
    }

    public void setPrimaryKey(Long id) {
        this.id = id;
    }
    
}
