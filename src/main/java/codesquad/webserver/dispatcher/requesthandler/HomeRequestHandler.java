package codesquad.webserver.dispatcher.requesthandler;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.annotation.Controller;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.db.article.Article;
import codesquad.webserver.db.article.ArticleDatabase;
import codesquad.webserver.db.user.User;
import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.dispatcher.view.ModelKey;
import codesquad.webserver.dispatcher.view.ViewName;
import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.session.Session;
import codesquad.webserver.session.SessionManager;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping(path = {"", "/", "/index", "/index.html"})
public class HomeRequestHandler extends AbstractRequestHandler {

    private static final String FILE_PATH = "/index.html";
    private static final Logger logger = LoggerFactory.getLogger(HomeRequestHandler.class);
    private static final int ARTICLE_PER_PAGE = 2;

    private final SessionManager sessionManager;
    private final ArticleDatabase articleDatabase;

    @Autowired
    public HomeRequestHandler(FileReader fileReader, SessionManager sessionManager, ArticleDatabase articleDatabase) {
        super(fileReader);
        this.sessionManager = sessionManager;
        this.articleDatabase = articleDatabase;
    }

    @Override
    protected ModelAndView handleGet(HttpRequest request) {
        try {
            FileReader.FileResource file = fileReader.read(FILE_PATH);
            String fileContent = file.readFileContent();
            ModelAndView mv = new ModelAndView(ViewName.TEMPLATE_VIEW);
            mv.addAttribute(ModelKey.CONTENT, fileContent);

            String sessionId = null;
            try {
                sessionId = request.getSessionIdFromRequest();
            } catch (IllegalStateException e) {
                logger.info("세션ID를 찾을 수 없는 요청입니다.: {}", e.getMessage());
            }

            User user = null;
            if (sessionId != null) {
                user = Optional.ofNullable(sessionManager.getSession(sessionId))
                        .map(Session::getUser)
                        .orElse(null);
            }

            if (user != null) {
                mv.addAttribute("isLoggedIn", true);
                mv.addAttribute("username", user.getName());
            } else {
                mv.addAttribute("isLoggedIn", false);
            }

            int page = getPageFromRequest(request);
            List<Article> articles = articleDatabase.findAllArticle(page, ARTICLE_PER_PAGE);
            int totalArticles = articleDatabase.getTotalArticleCount();
            List<Article> paginatedArticles = articles.stream()
                    .skip((page - 1) * ARTICLE_PER_PAGE)
                    .limit(ARTICLE_PER_PAGE)
                    .collect(Collectors.toList());
            logger.error("articles: {}", articles);
            mv.addAttribute(ModelKey.ARTICLES,paginatedArticles);
            mv.addAttribute(ModelKey.CURRENT_PAGE, page);
            mv.addAttribute(ModelKey.HAS_PREVIOUS_PAGE, page > 1);
            mv.addAttribute(ModelKey.HAS_NEXT_PAGE, page * ARTICLE_PER_PAGE < totalArticles);
            mv.addAttribute(ModelKey.PREVIOUS_PAGE, page - 1);
            mv.addAttribute(ModelKey.NEXT_PAGE, page + 1);

            return mv;
        } catch (IOException e) {
            logger.error("Error reading file: {}", e.getMessage());
            return new ModelAndView(ViewName.EXCEPTION_VIEW)
                    .addAttribute(ModelKey.STATUS_CODE, 404)
                    .addAttribute(ModelKey.ERROR_MESSAGE, "File not found");

        } catch (Exception e) {
            logger.error("Unhandled exception: {}", e.getMessage());
            return new ModelAndView(ViewName.EXCEPTION_VIEW)
                    .addAttribute(ModelKey.STATUS_CODE, 500)
                    .addAttribute(ModelKey.ERROR_MESSAGE, "Internal server error");
        }
    }

    private int getPageFromRequest(HttpRequest request) {
        String pageParam = request.getParams().get("page");
        if (pageParam == null) {
            return 1;
        }
        try {
            return Math.max(1, Integer.parseInt(pageParam));
        } catch (NumberFormatException e) {
            return 1;
        }
    }

}