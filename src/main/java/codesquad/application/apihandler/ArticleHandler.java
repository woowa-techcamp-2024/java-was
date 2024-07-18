package codesquad.application.apihandler;

import codesquad.application.datahandler.ArticleDataHandler;
import codesquad.application.domain.Article;
import codesquad.application.domain.User;
import codesquad.application.session.CookieExtractor;
import codesquad.application.session.SessionManager;
import codesquad.webserver.annotation.ApiHandler;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.annotation.Specify;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.util.FileWriter;
import codesquad.webserver.util.JsonStringConverter;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApiHandler
public class ArticleHandler {
    private final Logger log = LoggerFactory.getLogger(ArticleHandler.class);
    private final ArticleDataHandler articleDb;

    public ArticleHandler(
            @Specify("ArticleDataHandlerJdbc") ArticleDataHandler articleDb) {
        this.articleDb = articleDb;
    }

    @RequestMapping(method = HttpMethod.POST, path = "/write")
    public HttpResponse write(HttpRequest request) {
        Map<String, Object> body = request.getHttpBody();
        String title = (String) body.get("title");
        String content = (String) body.get("content");
        log.debug("[write] " + "title:" + title + ", content:" + content);

        String sid = CookieExtractor.getSid(request);
        User sessionUser = SessionManager.getInstance().getUser(sid);

        String fileName = "";
        byte[] fileBytes = (byte[]) body.get("image");
        fileName = (String) body.get("filename");
        FileWriter.saveFile(fileBytes, fileName);

        Article article = new Article(title, content, sessionUser.getNickname(), "./upload/" + fileName);
        log.debug("[write] " + article.toString());
        articleDb.insert(article);
        return HttpResponse.redirect("/index");
    }

    @RequestMapping(method = HttpMethod.GET, path = "/api/articles")
    public HttpResponse getAll(HttpRequest request) {
        List<Article> articles = articleDb.getAll();
        log.debug("[/api/articles] " + articles);
        return HttpResponse.ok(JsonStringConverter.collectionToJsonString(articles));
    }
}
