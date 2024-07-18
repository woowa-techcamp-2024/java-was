package codesquad.application.domain;

import java.time.LocalDateTime;

public class Article {
    private final String id;
    private final String title;
    private final String content;
    private final String author;
    private final String imagePath;
    private final LocalDateTime createdDt;

    public Article(String id, String title, String content, String author, String imagePath, LocalDateTime createdDt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.imagePath = imagePath;
        this.createdDt = createdDt;
    }

    public Article(String title, String content, String author, String imagePath) {
        this.id = null;
        this.title = title;
        this.content = content;
        this.author = author;
        this.imagePath = imagePath;
        this.createdDt = null;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getCreatedDt() {
        return createdDt;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", createdDt=" + createdDt +
                '}';
    }
}
