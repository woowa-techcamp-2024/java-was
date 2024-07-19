package codesquad.servlet.handler.api;

import codesquad.domain.model.Article;
import codesquad.domain.model.User;
import codesquad.utils.json.JsonMapper;
import java.util.List;

public class ArticleDetailResponse {

    private final Long id;
    private final String author;
    private final String title;
    private final String content;
    private final List<CommentResponse> commentResponses;
    private final String imagePath;

    public ArticleDetailResponse(Long id, String author, String title, String content,
                                 List<CommentResponse> commentResponses, String imagePath) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.commentResponses = commentResponses;
        this.imagePath = imagePath;
    }

    public static ArticleDetailResponse of(Article article, User user, List<CommentResponse> commentResponses) {
        return new ArticleDetailResponse(article.getId(), user.getName(), article.getTitle(), article.getContent(),
                commentResponses, article.getImagePath());
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\" : \"" + id + "\"," +
                "\"author\" : \"" + author + "\"," +
                "\"title\" : \"" + title + "\"," +
                "\"content\" : \"" + content + "\"," +
                "\"imagePath\" : \"" + imagePath + "\"," +
                "\"comments\" : " + JsonMapper.listToJson(commentResponses) +
                "}";
    }
}
