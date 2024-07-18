package model;

import java.util.List;

public class Article {
    private Long articleId;
    private final String title;
    private final String content;
    private final String userId;
    private final String photoPath;

    public Article(String title, String content, String userId, String photoPath) {
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.photoPath = photoPath;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUserId() {
        return userId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public List<Object> getComments() {
        return null;
    }
}
