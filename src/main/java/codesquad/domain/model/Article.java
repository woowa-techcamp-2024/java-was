package codesquad.domain.model;

public class Article {

    private Long id;
    private final Long authorId;
    private final String title;
    private final String content;
    private final String imagePath;

    public Article(Long authorId, String title, String content, String imagePath) {
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.imagePath = imagePath;
    }

    public Long getId() {
        return id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImagePath() {
        return imagePath;
    }

    /**
     * DAO 에서 PK Id 세팅 작업 이외에 사용 금지
     *
     * @param: primary key value
     */
    public void setPrimaryKey(final Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", authorId=" + authorId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
