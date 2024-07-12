package codesquad.handler;

import codesquad.error.HttpStatusException;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.MediaType;
import codesquad.http.StatusCode;
import codesquad.http.session.SessionContextHolder;
import codesquad.http.session.SessionManager;
import codesquad.model.User;
import codesquad.model.UserDataBase;
import codesquad.resource.DirectoryIndexResolver;
import codesquad.resource.Resource;

import java.util.Optional;

public class DynamicResourceHandler extends RequestHandler {

    private static DynamicResourceHandler instance;

    private final DirectoryIndexResolver directoryIndexResolver = DirectoryIndexResolver.getInstance();
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final UserDataBase userDataBase = UserDataBase.getInstance();

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
        Optional<Resource> resolvedResource = directoryIndexResolver.resolve(uri);
        if (resolvedResource.isEmpty()) {
            throw new HttpStatusException(StatusCode.NOT_FOUND, "[ERROR] 파일을 찾을 수 없습니다.");
        }

        Resource resource = resolvedResource.get();
        if (!resource.getExtension().equals("html")) {
            return responseGenerator.sendOK(resource.getContent(), MediaType.find(resource.getExtension()), httpRequest);
        }

        String sessionId = SessionContextHolder.getSessionId();
        String foundUserId = sessionManager.findUserId(sessionId)
                .orElse("");
        Optional<User> foundUser = userDataBase.findUser(foundUserId);
        String content = new String(resource.getContent());
        if (foundUser.isEmpty()) {
            return responseGenerator.sendOK(content.getBytes(), MediaType.TEXT_HTML, httpRequest);
        }

        User user = foundUser.get();
        String replacedHtml = HtmlTransformer.replaceUserHeader(content, user);
        return responseGenerator.sendOK(replacedHtml.getBytes(), MediaType.TEXT_HTML, httpRequest);
    }
}
