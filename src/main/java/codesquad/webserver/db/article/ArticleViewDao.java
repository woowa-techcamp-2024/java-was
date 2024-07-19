package codesquad.webserver.db.article;

import codesquad.webserver.db.user.User;

public class ArticleViewDao {
    private final long id;
    private final String title;
    private final String content;
    private final User user;
    private final Image image;

    public ArticleViewDao(long id, String title, String content, User user, Image image) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "ArticleViewDao{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", user=" + user +
                ", image=" + image +
                '}';
    }
}
