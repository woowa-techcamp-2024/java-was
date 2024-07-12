package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.MediaType;
import codesquad.http.session.SessionContextHolder;
import codesquad.http.session.SessionManager;
import codesquad.model.User;
import codesquad.model.UserDataBase;
import codesquad.resource.DirectoryIndexResolver;
import codesquad.resource.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
            return responseGenerator.sendNotFound(httpRequest);
        }

        Resource resource = resolvedResource.get();
        if (!resource.getExtension().equals("html")) {
            return responseGenerator.sendOK(resource.getContent(), MediaType.find(resource.getExtension()), httpRequest);
        }

        String sessionId = SessionContextHolder.getSessionId();
        String foundUserId = sessionManager.findUserId(sessionId)
                .orElse("");
        Optional<User> foundUser = userDataBase.findUser(foundUserId);

        String replacedHtml = replaceHtml(resource, foundUser);
        return responseGenerator.sendOK(replacedHtml.getBytes(), MediaType.TEXT_HTML, httpRequest);
    }

    private static String replaceHtml(Resource resource, Optional<User> foundUser) {
        byte[] content = resource.getContent();
        String htmlText = new String(content);
        StringBuilder replacedHtml = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new StringReader(htmlText))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("id=\"headerButton\"") && foundUser.isPresent()) {
                    line = line.replace("로그인", "로그아웃");
                    line = line.replace("/login", "/user/logout");
                }
                if (line.contains("id=\"headerLabel\"") && foundUser.isPresent()) {
                    User user = foundUser.get();
                    line = line.replace("회원 가입", user.getNickname());
                    line = line.replace("href=\"/registration\"", "");
                }
                replacedHtml.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return replacedHtml.toString();
    }
}
