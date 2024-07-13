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

import java.util.List;
import java.util.Optional;

public class UserListHandler extends RequestHandler {

    private static UserListHandler instance;

    private final UserDataBase userDataBase = UserDataBase.getInstance();
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final DirectoryIndexResolver directoryIndexResolver = DirectoryIndexResolver.getInstance();

    private UserListHandler() {
    }

    public static UserListHandler getInstance() {
        if (instance == null) {
            instance = new UserListHandler();
        }
        return instance;
    }

    @Override
    protected HttpResponse handleGet(HttpRequest httpRequest) {
        String sessionId = SessionContextHolder.getSessionId();
        String foundUserId = sessionManager.findUserId(sessionId)
                .orElse("");
        Optional<User> foundUser = userDataBase.findUser(foundUserId);
        if (foundUser.isEmpty()) {
            return responseGenerator.sendRedirect(httpRequest, "/login");
        }

        Resource resource = directoryIndexResolver.resolve("/user")
                .orElseThrow(() -> new HttpStatusException(StatusCode.NOT_FOUND));
        String content = new String(resource.getContent());
        List<User> users = userDataBase.findAll();

        String replacedContent = HtmlTransformer.appendUserList(content, users);
        replacedContent = HtmlTransformer.replaceUserHeader(replacedContent, foundUser.get());
        return responseGenerator.sendOK(replacedContent.getBytes(), MediaType.TEXT_HTML, httpRequest);
    }
}
