package codesquad.servlet.handler.api;

import codesquad.domain.ArticleStorage;
import codesquad.domain.CommentStorage;
import codesquad.domain.UserStorage;
import codesquad.domain.model.Article;
import codesquad.domain.model.Comment;
import codesquad.domain.model.User;
import codesquad.servlet.execption.ClientException;
import codesquad.servlet.handler.Handler;
import codesquad.utils.json.JsonMapper;
import codesquad.webserver.http.HttpMediaType;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleListHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(ArticleListHandler.class);

    private final ArticleStorage articleStorage;
    private final UserStorage userStorage;
    private final CommentStorage commentStorage;

    public ArticleListHandler(ArticleStorage articleStorage, UserStorage userStorage, CommentStorage commentStorage) {
        this.articleStorage = articleStorage;
        this.userStorage = userStorage;
        this.commentStorage = commentStorage;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        logger.debug("ArticleListHandler start");
        List<Article> articles = articleStorage.selectAll();

        List<User> users = new ArrayList<>();
        for (Article article : articles) {
            User author = userStorage.selectById(article.getAuthorId())
                    .orElseThrow(() -> new ClientException("존재하지 않는 ID 사용자입니다", HttpStatus.BAD_REQUEST));
            users.add(author);
        }

        List<ArticleDetailResponse> articleDetailResponses = new ArrayList<>();
        for (int idx = 0; idx < articles.size(); idx++) {
            Article article = articles.get(idx);
            User author = users.get(idx);

            List<CommentResponse> commentResponses = new ArrayList<>();
            List<Comment> comments = commentStorage.selectByArticleId(article.getId());
            for (Comment comment : comments) {
                User commenter = userStorage.selectById(comment.getCommenterId()).orElseThrow();
                CommentResponse commentResponse = CommentResponse.of(comment, commenter);
                commentResponses.add(commentResponse);
            }

            articleDetailResponses.add(ArticleDetailResponse.of(article, author, commentResponses));
        }

        String json = JsonMapper.listToJson(articleDetailResponses);
        response.setContentType(HttpMediaType.APPLICATION_JSON);
        response.setBody(json.getBytes());
        logger.debug("ArticleListHandler end");
    }

}
