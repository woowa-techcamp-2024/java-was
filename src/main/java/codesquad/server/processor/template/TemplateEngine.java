package codesquad.server.processor.template;

import codesquad.domain.entity.Comment;
import codesquad.domain.entity.Post;
import codesquad.domain.entity.User;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static codesquad.Main.*;
import static codesquad.server.thread.ThreadManager.session;

public class TemplateEngine {
    public static byte[] convert(byte[] response, String id) {
        String responseString = new String(response);
        if (responseString.matches("(?s).*\\{\\{if post}}.*?\\{\\{endif}}.*")) responseString = convertIfPost(responseString, id);
        if (responseString.matches("(?s).*\\{\\{nickname}}.*")) responseString = convertNickName(responseString);
        if (responseString.matches("(?s).*\\{\\{userList}}.*")) responseString = convertUserList(responseString);
        if (responseString.matches("(?s).*\\{\\{title}}.*")) responseString = convertTitle(responseString, id);
        if (responseString.matches("(?s).*\\{\\{post}}.*")) responseString = convertPost(responseString, id);
        if (responseString.matches("(?s).*\\{\\{image}}.*")) responseString = convertImage(responseString, id);
        if (responseString.matches("(?s).*\\{\\{commentList}}.*")) responseString = convertCommentList(responseString, id);
        if (responseString.matches("(?s).*\\{\\{account}}.*")) responseString = convertAccount(responseString, id);
        return responseString.getBytes();
    }

    private static String convertIfPost(String response, String id) {
        Post post = postDatabase.getById(Integer.parseInt(id));
        if (post == null) {
            String pattern = "\\{\\{if post}}.*?\\{\\{endif}}";
            Pattern regex = Pattern.compile(pattern, Pattern.DOTALL);
            Matcher matcher = regex.matcher(response);
            response = matcher.replaceAll("");
        }
        response = response.replace("{{if post}}", "");
        response = response.replace("{{endif}}", "");
        return response;
    }

    private static String convertAccount(String response, String id) {
        Post post = postDatabase.getById(Integer.parseInt(id));
        if (post == null) return response.replace("{{account}}", "");
        User user = userDatabase.getById(post.userid());
        return response.replace("{{account}}", user.nickname());
    }

    private static String convertCommentList(String response, String id) {
        StringBuilder commentList = new StringBuilder();
        Map<Integer, Comment> commentMap = commentDatabase.getAll();
        if (commentMap == null) return response.replace("{{commentList}}", "");

        for (var comment : commentMap.values()) {
            if (Objects.equals(comment.postid(), id)) {
                User user = userDatabase.getById(comment.userid());
                commentList.append("<li class=\"comment-item hidden\">\n")
                        .append("<div class=\"comment__item__user\">\n")
                        .append("<img class=\"comment__item__user__img\" />")
                        .append("<p class=\"comment__item__user__nickname\">").append(user.nickname()).append("</p>\n")
                        .append("</div>\n")
                        .append("<p class=\"comment__item__article\">").append(comment.contents()).append("</p>\n")
                        .append("</li>\n");
            }
        }
        return response.replace("{{commentList}}", commentList.toString());
    }

    private static String convertNickName(String response) {
        return response.replace("{{nickname}}", sessionDatabase.getById(session.get()).nickname());
    }

    private static String convertUserList(String response) {
        StringBuilder userList = new StringBuilder();
        Map<String, User> userMap = userDatabase.getAll();
        if (userMap == null) return response.replace("{{userList}}", "");

        for (User user : userMap.values()) {
            userList.append("<li class=\"user-item\">\n")
                    .append("<div class=\"user-name\">").append(user.userid()).append("</div>\n")
                    .append("<div class=\"user-nickname\">").append(user.nickname()).append("</div>\n")
                    .append("</li>\n");
        }
        return response.replace("{{userList}}", userList.toString());
    }

    private static String convertTitle(String response, String id) {
        Post post = postDatabase.getById(Integer.parseInt(id));
        if (post == null) return response.replace("{{title}}", "작성한 글이 없습니다!");
        return response.replace("{{title}}", post.title());
    }

    private static String convertPost(String response, String id) {
        Post post = postDatabase.getById(Integer.parseInt(id));
        if (post == null) return response.replace("{{post}}", "");
        return response.replace("{{post}}", post.contents());
    }

    private static String convertImage(String response, String id) {
        Post post = postDatabase.getById(Integer.parseInt(id));
        if (post == null) return response.replace("{{image}}", "<img class=\"post__img\" />");
        return response.replace("{{image}}", "<img class=\"post__img\" src=\"/upload/" + post.image() + "\" />");
    }
}