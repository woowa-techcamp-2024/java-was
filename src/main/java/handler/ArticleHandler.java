package handler;

import dto.WriteRequestDto;
import exception.BadRequestException;
import exception.InternalServerError;
import exception.NotFoundException;
import http.HttpMultiPartRequest;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import http.startline.RequestLine;
import java.sql.SQLException;
import java.util.Map;
import model.Article;
import org.slf4j.Logger;
import repository.ArticleRepositoryDB;
import session.Session;
import util.LoggerUtil;
import util.PhotoReader;
import util.RequestContext;
import util.StaticPage;

public class ArticleHandler extends MyHandler {

    private static final Logger logger = LoggerUtil.getLogger();

    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        // index
        // getArticleId로 들어온 요청을 받아서 해당 id를 가진 글을 보여준다.

        RequestLine requestLine = (RequestLine) httpRequest.getStartLine();

        requestLine.getUrlPath().getQueryParameter("articleId").ifPresentOrElse(
            articleId -> {
                Article article = null;
                try {
                    article = ArticleRepositoryDB.getInstance().findById(Long.parseLong(articleId)).orElse(null);
                } catch (SQLException e) {
                    logger.error("DB에서 글을 찾는 중 오류가 발생했습니다. {}", e.getMessage());
                    ResponseValueSetter.failRedirect(httpResponse, new NotFoundException());
                    return;
                }
                if (article == null) {
                    ResponseValueSetter.failRedirect(httpResponse, new NotFoundException());
                    return;
                }

                ResponseValueSetter.success(httpResponse, makeArticleHtml(article));
            },
            () -> {
                ResponseValueSetter.fail(httpResponse, new BadRequestException("잘못된 요청입니다."));
            }
        );


    }

    private byte[] makeArticleHtml(Article article) {
        String html =
            "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>" + article.getTitle() + "</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4; }" +
                ".article { max-width: 800px; margin: 0 auto; background: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }" +
                "h1 { font-size: 24px; color: #333; }" +
                "p { font-size: 16px; color: #666; line-height: 1.6; }" +
                "img { max-width: 100%; height: auto; border-radius: 5px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='article'>" +
                "<h1>" + "제목: " + article.getTitle() + "</h1>" +
                "<p>" + "내용: " + article.getContent() + "</p>" +
                (article.getPhotoPath() != null && !article.getPhotoPath().isEmpty() ? "<img src=\"" + article.getPhotoPath() + "\" alt=\"Article Photo\"/>" : "") +
                "</div>" +
                "</body>" +
                "</html>";
        return html.getBytes();
    }


    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws SQLException {
        // login
        // 글 작성
        // body를 통해 글 정보를 가져온다.
        // 이후 해당 글을 작성한다.
        // 보안 로직 여기서

        HttpMultiPartRequest httpMultiPartRequest = (HttpMultiPartRequest) httpRequest;
        Map<String, Map<String, byte[]>> bodyParams = httpMultiPartRequest.multipartBodyParams();

        String title = new String(bodyParams.get("title").get("content"));
        String content = new String(bodyParams.get("content").get("content"));
        byte[] photo = bodyParams.get("photo").get("content");
        Session session = RequestContext.current().getSession().orElse(null);

        if (
            bodyParams.get("title") == null ||
                bodyParams.get("content") == null ||
                bodyParams.get("photo") == null
        ) {
            ResponseValueSetter.failRedirect(httpResponse, new BadRequestException("맞는 형식으로 입력해주세요."));
            return;
        }

        if (session == null || session.getUser() == null) {
            ResponseValueSetter.fail(httpResponse, new BadRequestException("로그인이 필요합니다."));
            return;
        }

        if (!validateBodyParams(title, content, photo)) {
            ResponseValueSetter.failRedirect(httpResponse,
                new BadRequestException("맞는 형식으로 입력해주세요."));
            return;
        }

        String filePath;
        try {
            filePath = PhotoReader.savePhoto(httpMultiPartRequest.getFileExtension("photo"), photo);
        } catch (Exception e) {
            ResponseValueSetter.failRedirect(httpResponse, new InternalServerError("사진 저장에 실패했습니다."));
            logger.error("사진 저장에 실패했습니다. {}", e.getMessage());
            return;
        }

        // 주소 추가
        WriteRequestDto writeRequestDto = new WriteRequestDto(title, content, filePath);
        // 글 작성 로직

        Article article = writeRequestDto.toEntity(session.getUser());
        ArticleRepositoryDB.getInstance().save(article);

        ResponseValueSetter.redirect(httpRequest, httpResponse, StaticPage.indexPage);
    }

    private boolean validateBodyParams(
        String title,
        String content,
        byte[] photo
    ) {
        return title != null && !title.isEmpty() &&
            content != null && !content.isEmpty() &&
            photo != null && photo.length > 0;
    }
}
