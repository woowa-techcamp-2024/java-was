package codesquad.servlet.handler.api;

import codesquad.domain.ArticleStorage;
import codesquad.domain.CommentStorage;
import codesquad.domain.model.Article;
import codesquad.domain.model.Comment;
import codesquad.domain.model.User;
import codesquad.servlet.SessionStorage;
import codesquad.servlet.handler.Handler;
import codesquad.webserver.http.Cookie;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleCommentWriteHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(ArticleCommentWriteHandler.class);

    private final ArticleStorage articleStorage;
    private final SessionStorage sessionStorage;
    private final CommentStorage commentStorage;

    public ArticleCommentWriteHandler(ArticleStorage articleStorage, SessionStorage sessionStorage,
                                      CommentStorage commentStorage) {
        this.articleStorage = articleStorage;
        this.sessionStorage = sessionStorage;
        this.commentStorage = commentStorage;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        logger.debug("ArticleCommentHandler start");

        Cookie cookie = request.getCookie();
        String uuid = cookie.getValue();
        User commenter = sessionStorage.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("세션에 존재하지 않습니다."));

        Map<String, String> queryStrings = request.getQueryStrings();
        long articleId = Long.parseLong(queryStrings.get("articleId"));

        String content = request.getBodyParamValue("content");

        Article article = articleStorage.selectById(articleId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 게시글 ID입니다"));

        Comment comment = new Comment(commenter.getId(), article.getId(), content);
        commentStorage.insert(comment);
        List<Comment> comments = commentStorage.selectByArticleId(article.getId());
        logger.debug("comments = {}", comments);
        response.sendRedirect("/");
        logger.debug("ArticleCommentHandler end");
    }
}
