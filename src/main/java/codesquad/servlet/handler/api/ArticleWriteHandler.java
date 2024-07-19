package codesquad.servlet.handler.api;

import codesquad.domain.ArticleStorage;
import codesquad.domain.model.Article;
import codesquad.domain.model.User;
import codesquad.infra.ImageWriter;
import codesquad.servlet.SessionStorage;
import codesquad.servlet.handler.Handler;
import codesquad.webserver.http.Cookie;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.http.MultiPartFormData;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleWriteHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(ArticleWriteHandler.class);
    private static final String IMAGE_DIRECTORY = "static/article/img";

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

        String filename = request.getBodyParamValue("filename");
        MultiPartFormData multiPartFormData = request.getMultiPartFormData("image");

        byte[] imageContent = multiPartFormData.getContent();
        String imagePath = null;
        if (filename != null && !filename.isEmpty() && imageContent != null) {
            imagePath = ImageWriter.write(filename, imageContent);
        }
        Cookie cookie = request.getCookie();
        String uuid = cookie.getValue();

        User user = sessionStorage.getSessionValue(uuid);
        Article article = new Article(user.getId(), title, content, imagePath);

        Long id = articleStorage.insert(article);
        Optional<Article> saveArticle = articleStorage.selectById(id);
        logger.debug("save article = {}", saveArticle);
        logger.debug("ArticleWriteHandler end");
        response.sendRedirect("/");
    }

}
