package codesquad.webserver.db.article;

import codesquad.webserver.db.user.User;

public class Article {
    private Long id;
    private String title;
    private String content;
    private User author;
    private Image image;

    public Article(String title, String content) {
        this(null, title, content);
    }

    public Article(String title, String content, User author) {
        this(null, title, content, author, null);
    }

    public Article(String title, String content, User author, Image image) {
        this(null, title, content, author, image);
    }

    public Article(Long id, String title, String content, User author, Image image) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.image = image;
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

    public Image getImage() {
        return image;
    }

    public Article setImage(Image image) {
        return new Article(this.id, this.title, this.content, this.author, image);
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", image=" + image +
                '}';
    }
}