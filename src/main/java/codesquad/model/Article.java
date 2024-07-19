package codesquad.model;

public class Article {

    private final Long id;
    private final String content;
    private final String imageName;
    private final Long userId;

    public Article(Long id, String content, String imageName, Long userId) {
        this.id = id;
        this.content = content;
        this.imageName = imageName;
        this.userId = userId;
    }

    public Article(String content, String imageName, Long userId) {
        this(null, content, imageName, userId);
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getImageName() {
        return imageName;
    }

    public Long getUserId() {
        return userId;
    }
}
