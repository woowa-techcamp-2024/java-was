package codesquad.handler;

import codesquad.database.ArticleRepository;
import codesquad.error.HttpRequestException;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.MediaType;
import codesquad.http.MultipartFile;
import codesquad.http.StatusCode;
import codesquad.http.session.SessionContext;
import codesquad.http.session.SessionContextHolder;
import codesquad.model.Article;
import codesquad.model.User;
import codesquad.resource.DirectoryIndexResolver;
import codesquad.resource.ImageWriter;
import codesquad.resource.Resource;
import codesquad.resource.transform.HtmlTransformer;

import java.util.Map;

public class ArticleHandler extends AuthenticatedHandler {

    private static ArticleHandler instance;

    private final DirectoryIndexResolver directoryIndexResolver = DirectoryIndexResolver.getInstance();
    private final ObjectMapper objectMapper = ObjectMapper.getInstance();
    private final ArticleRepository articleRepository = ArticleRepository.getInstance();

    private ArticleHandler() {
    }

    public static ArticleHandler getInstance() {
        if (instance == null) {
            instance = new ArticleHandler();
        }
        return instance;
    }

    @Override
    protected HttpResponse handleGet(HttpRequest httpRequest) {
        String uri = httpRequest.uri();
        Resource resource = directoryIndexResolver.resolve(uri)
                .orElseThrow(() -> new HttpRequestException(StatusCode.NOT_FOUND, "[ERROR] 파일을 찾을 수 없습니다."));
        String content = new String(resource.getContent());
        SessionContext context = SessionContextHolder.getContext();
        String replacedHtml = HtmlTransformer.replaceUserHeader(content, context.user());
        return responseGenerator.sendOK(replacedHtml.getBytes(), MediaType.find(resource.getExtension()), httpRequest);
    }

    @Override
    protected HttpResponse handlePost(HttpRequest httpRequest) {
        Map<String, Object> multipart = httpRequest.getMultipart()
                .orElseThrow(() -> new HttpRequestException(StatusCode.BAD_REQUEST, "[ERROR] 요청 바디가 비어있습니다."));
        String content = (String) multipart.get("content");
        if (content.isBlank()) {
            throw new HttpRequestException(StatusCode.BAD_REQUEST, "[ERROR] 글 내용이 비어있습니다.");
        }
        MultipartFile imageFile = (MultipartFile) multipart.get("image");
        Resource imageResource = Resource.file(imageFile.filename(), imageFile.content());
        String fileName = ImageWriter.write(imageResource);

        User user = SessionContextHolder.getContext()
                .user();
        Article article = new Article(content, fileName, user.getId());
        articleRepository.save(article);
        return responseGenerator.sendRedirect(httpRequest, "/");
    }
}
