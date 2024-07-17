package codesquad.webserver.db.article;

import codesquad.webserver.db.user.User;
import java.util.ArrayList;
import java.util.List;

public class Article {
    private Long id;
    private String title;
    private String content;
    private User author;
    private List<Image> images = new ArrayList<>();

    public Article(String title, String content) {
        this(null, title, content);
    }

    public Article(String title, String content, User author) {
        this(null, title, content, author, new ArrayList<>());
    }

    public Article(Long id, String title, String content, User author, List<Image> images) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.images = images;
    }

    public Article(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void addImage(Image image) {
        images.add(image);
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", images=" + images +
                '}';
    }
}