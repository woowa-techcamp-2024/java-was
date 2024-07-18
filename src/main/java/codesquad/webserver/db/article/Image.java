package codesquad.webserver.db.article;

public class Image {
    private Long id;
    private String path;
    private String filename;
    private Long articleId;

    public Image(Long id, String path, String filename, Long articleId) {
        this.id = id;
        this.path = path;
        this.filename = filename;
        this.articleId = articleId;
    }

    public Image(String path, String filename, Long articleId) {
        this(null, path, filename, articleId);
    }

    public Long getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public String getPath() {
        return path;
    }

    public Long getArticleId() {
        return articleId;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", filename='" + filename + '\'' +
                ", articleId=" + articleId +
                '}';
    }
}