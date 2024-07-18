package handler;

import static util.HeaderStringUtil.CONTENT_TYPE;

import http.Header;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import http.startline.RequestLine;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import model.Article;
import model.User;
import org.slf4j.Logger;
import repository.ArticleRepository;
import repository.ArticleRepositoryDB;
import session.Session;
import util.FileReader;
import util.LoggerUtil;
import util.RequestContext;
import util.StaticPage;

public class StaticHandler extends MyHandler {

    private static final Logger logger = LoggerUtil.getLogger();


    @Override
    void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException, SQLException {
        RequestLine requestLine = (RequestLine) httpRequest.getStartLine();
        Header responseHeader = httpResponse.getHeader();

        if (requestLine.getUrlPath().getPath().equals(StaticPage.indexPage)) {
            // context로 로그인 여부 확인
            callIndexPage(httpRequest, httpResponse);
            return;
        }

        if (requestLine.getUrlPath().getPath().equals("/register.html")) {
            logger.info("redirect to {}", StaticPage.registerPage);
            ResponseValueSetter.redirect(httpRequest, httpResponse, StaticPage.registerPage);
            return;
        }

        byte[] fileContent = FileReader.readFileFromUrlPath(requestLine.getUrlPath());

        if(fileContent == null) {
            return;
        }

        String contentType = FileReader.guessContentTypeFromUrlPath(requestLine.getUrlPath());
        responseHeader.addKey(CONTENT_TYPE, contentType);

        //httpResponse.;
        ResponseValueSetter.success(httpResponse, fileContent);
    }

    private void callIndexPage(HttpRequest httpRequest, HttpResponse httpResponse)
        throws IOException, SQLException {
        // 1. index.html 불러오기
        byte[] fileContent = FileReader.readFileFromUrlPath(RequestContext.current().getUrlPath());
        String fileString = new String(fileContent);
        String contentType = FileReader.guessContentTypeFromUrlPath(
            RequestContext.current().getUrlPath());
        Header responseHeader = httpResponse.getHeader();
        responseHeader.addKey(CONTENT_TYPE, contentType);

        // 2. 로그인 여부 확인 -> 추가할 파일 불러오기
        String fileToAdd = stringToAdd();
        String articleToAdd = articleToAdd();

        logger.info("fileToAdd: {}", fileToAdd);

        // 3. 파일 합치기
        String combinedString = fileString.replace("{{header-menu}}", fileToAdd);
        combinedString = combinedString.replace("{{article-title-list}}", articleToAdd);

        // 4. response에 담기
        ResponseValueSetter.success(httpResponse, combinedString.getBytes());
    }

    private String articleToAdd() throws SQLException {
        ArticleRepository articleRepository = ArticleRepositoryDB.getInstance();

        // Retrieve all articles and format them into an HTML structure
        return ((ArticleRepositoryDB) articleRepository).getAllArticles().entrySet().stream()
            .map(entry -> formatArticleAsHtml(entry.getValue()))
            .reduce("", (a, b) -> a + b);
    }

    // Helper method to format a single article as HTML
    public String formatArticleAsHtml(Article article) {
        StringBuilder html = new StringBuilder();
        html.append("<a class=\"article\" href=\"/article?articleId=").append(article.getArticleId()).append("\" style=\"text-decoration: none; color: inherit; display: block; width: 80%; margin: 0 auto;\">");
        html.append("<div class=\"wrapper\" style=\"border: 1px solid #ddd; padding: 10px; margin-bottom: 20px; border-radius: 5px; display: flex; align-items: center; width: 100%; box-sizing: border-box; flex-direction: row;\">");
        if (article.getPhotoPath() != null && !article.getPhotoPath().isEmpty()) {
            html.append("<img src=\"").append(article.getPhotoPath()).append("\" alt=\"Article Photo\" style=\"width: 100px; height: auto; border-radius: 5px; margin-right: 20px;\" />");
        }
        html.append("<div class=\"article-content\" style=\"flex: 1;\">");
        html.append("<h2 style=\"font-size: 18px; margin: 0;\">").append(article.getTitle()).append("</h2>");
        html.append("<p style=\"margin: 5px 0 0; font-size: 14px; color: #555;\"><strong>글쓴이:</strong> ").append(article.getUserId()).append("</p>");
        html.append("</div>");
        html.append("</div>");
        html.append("</a>");
        return html.toString();
    }


    private String stringToAdd() throws IOException {
        String failedPath = "index/logout-menu.html";
        String successPath = "index/login-menu.html";
        Optional<User> user = RequestContext.current().getSession()
            .flatMap(session -> Optional.ofNullable(
                (User) session.getAttribute(Session.USER)
            ));

        logger.info("user: {}", user);
        if (user.isPresent()) {
            logger.info("logged in");
            String ret = new String(FileReader.readFileFromUrlPath(successPath));
            return ret.replace("{username}", user.get().getName());
        }

        logger.info("not logged in");
        return new String(FileReader.readFileFromUrlPath(failedPath));
    }
}
