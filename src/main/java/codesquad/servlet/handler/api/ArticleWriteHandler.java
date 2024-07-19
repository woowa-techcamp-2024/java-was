package codesquad.servlet.handler.api;

import codesquad.domain.ArticleStorage;
import codesquad.domain.model.Article;
import codesquad.domain.model.User;
import codesquad.servlet.SessionStorage;
import codesquad.servlet.handler.Handler;
import codesquad.webserver.http.Cookie;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleWriteHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(ArticleWriteHandler.class);

    private final SessionStorage sessionStorage;
    private final ArticleStorage articleStorage;

    public ArticleWriteHandler(SessionStorage sessionStorage, ArticleStorage articleStorage) {
        this.sessionStorage = sessionStorage;
        this.articleStorage = articleStorage;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        logger.debug("ArticleWriteHandler start");
        String title = request.getBodyParamValue("title");
        String content = request.getBodyParamValue("content");

        Cookie cookie = request.getCookie();
        String uuid = cookie.getValue();

        User user = sessionStorage.getSessionValue(uuid);
        Article article = new Article(user.getId(), title, content);

        Long id = articleStorage.insert(article);
        Optional<Article> saveArticle = articleStorage.selectById(id);
        logger.debug("save article = {}", saveArticle);
        logger.debug("ArticleWriteHandler end");
        response.sendRedirect("/");
    }

}
