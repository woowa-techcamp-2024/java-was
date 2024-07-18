package codesquad.model;

public class Article {

    private final Long id;
    private final String content;
    private final Long userId;

    public Article(Long id, String content, Long userId) {
        this.id = id;
        this.content = content;
        this.userId = userId;
    }

    public Article(String content, Long userId) {
        this(null, content, userId);
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Long getUserId() {
        return userId;
    }
}
