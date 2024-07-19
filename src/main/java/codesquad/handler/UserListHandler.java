package codesquad.handler;

import codesquad.database.UserRepository;
import codesquad.error.HttpRequestException;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.MediaType;
import codesquad.http.StatusCode;
import codesquad.http.session.SessionContext;
import codesquad.http.session.SessionContextHolder;
import codesquad.model.User;
import codesquad.resource.DirectoryIndexResolver;
import codesquad.resource.Resource;
import codesquad.resource.transform.HtmlTransformer;

import java.util.List;

public class UserListHandler extends AuthenticatedHandler {

    private static UserListHandler instance;

    private final UserRepository userRepository = UserRepository.getInstance();
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
        Resource resource = directoryIndexResolver.resolve("/user/index.html")
                .orElseThrow(() -> new HttpRequestException(StatusCode.NOT_FOUND));
        String content = new String(resource.getContent());
        List<User> users = userRepository.findAll();

        SessionContext sessionContext = SessionContextHolder.getContext();
        String replacedContent = HtmlTransformer.replaceUserHeader(content, sessionContext.user());
        replacedContent = HtmlTransformer.appendUserList(replacedContent, users);
        return responseGenerator.sendOK(replacedContent.getBytes(), MediaType.TEXT_HTML, httpRequest);
    }
}
