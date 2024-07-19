package codesquad.handler;

import codesquad.database.ArticleRepository;
import codesquad.database.UserRepository;
import codesquad.error.HttpRequestException;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.MediaType;
import codesquad.http.StatusCode;
import codesquad.http.session.SessionContext;
import codesquad.http.session.SessionContextHolder;
import codesquad.model.Article;
import codesquad.model.User;
import codesquad.resource.DirectoryIndexResolver;
import codesquad.resource.ImageReader;
import codesquad.resource.Resource;
import codesquad.resource.transform.HtmlTransformer;

import java.util.List;
import java.util.Objects;

public class DynamicResourceHandler extends RequestHandler {

    private static DynamicResourceHandler instance;

    private final DirectoryIndexResolver directoryIndexResolver = DirectoryIndexResolver.getInstance();
    private final ArticleRepository articleRepository = ArticleRepository.getInstance();
    private final UserRepository userRepository = UserRepository.getInstance();

    private DynamicResourceHandler() {
    }

    public static DynamicResourceHandler getInstance() {
        if (instance == null) {
            instance = new DynamicResourceHandler();
        }
        return instance;
    }

    @Override
    protected HttpResponse handleGet(HttpRequest httpRequest) {
        String uri = httpRequest.uri();
        Resource resource = directoryIndexResolver.resolve(uri)
                .orElseGet(() -> ImageReader.read(uri));
        if (Objects.isNull(resource)) {
            throw new HttpRequestException(StatusCode.NOT_FOUND, "[ERROR] 파일을 찾을 수 없습니다.");
        }

        if (!resource.getExtension().equals("html")) {
            return responseGenerator.sendOK(resource.getContent(), MediaType.find(resource.getExtension()), httpRequest);
        }

        String content = new String(resource.getContent());
        SessionContext sessionContext = SessionContextHolder.getContext();
        String replacedHtml = HtmlTransformer.replaceUserHeader(content, sessionContext.user());
        // FIXME: refactoring 하기
        List<Article> articles = articleRepository.findAll();
        List<User> users = userRepository.findAll();
        List<User> writers = articles.stream()
                .map(article -> users.stream()
                        .filter(user -> Objects.equals(user.getId(), article.getUserId()))
                        .findAny()
                        .orElse(null))
                .toList();
        replacedHtml = HtmlTransformer.appendArticles(replacedHtml, articles, writers);
        return responseGenerator.sendOK(replacedHtml.getBytes(), MediaType.TEXT_HTML, httpRequest);
    }
}
