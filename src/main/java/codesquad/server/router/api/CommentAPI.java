package codesquad.server.router.api;

import codesquad.domain.entity.Comment;
import codesquad.domain.entity.User;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.server.router.handler.ResourceHandler;

import java.util.Map;

import static codesquad.Main.*;
import static codesquad.server.thread.ThreadManager.session;

public class CommentAPI implements ResourceHandler {
    @Override
    public HttpResponse handle(HttpRequest request) {
        User user = sessionDatabase.selectById(session.get());
        Map<String, String> contents = request.getBody().getBodyMap();
        commentDatabase.insert(new Comment(user.userid(), contents.get("id"), contents.get("contents")));
        return handleRedirect("/main/index.html?id=" + contents.get("id"));
    }
}
