package codesquad.resource.transform;

import codesquad.model.Article;
import codesquad.model.User;
import codesquad.resource.Resource;
import codesquad.resource.ResourcesReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Objects;

public class HtmlTransformer {

    public static String replaceUserHeader(String originHtml, User user) {
        try (BufferedReader reader = new BufferedReader(new StringReader(originHtml))) {
            StringBuilder replacedHtml = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("id=\"writeButton\"") && user == null) {
                    line = "";
                }
                if (line.contains("id=\"headerButton\"") && user != null) {
                    line = line.replace("로그인", "로그아웃");
                    line = line.replace("/login", "/user/logout");
                }
                if (line.contains("id=\"headerLabel\"") && user != null) {
                    line = line.replace("회원 가입", user.getNickname());
                    line = line.replace("href=\"/registration\"", "");
                }
                replacedHtml.append(line)
                        .append("\n");
            }
            return replacedHtml.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String appendUserList(String originHtml, List<User> users) {
        try (BufferedReader reader = new BufferedReader(new StringReader(originHtml))) {
            StringBuilder replacedHtml = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("id=\"userList\"")) {
                    for (int i = 0; i < users.size(); i++) {
                        User user = users.get(i);
                        replacedHtml.append("<tr>");
                        replacedHtml.append("<th scope=\"row\">").append(i + 1).append("</th>");
                        replacedHtml.append("<td>").append(user.getUserId()).append("</td>");
                        replacedHtml.append("<td>").append(user.getNickname()).append("</td>");
                        replacedHtml.append("</tr>");
                    }
                } else {
                    replacedHtml.append(line);
                }
            }
            return replacedHtml.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String appendArticles(String originHtml, List<Article> articles, List<User> users) {
        Resource resource = ResourcesReader.readResource("/article/component.html")
                .orElseThrow(() -> new IllegalStateException("[ERROR] 게시글 컴포넌트를 읽어오는데 실패했습니다."));
        String content = new String(resource.getContent());
        StringBuilder articlesComponent = new StringBuilder();
        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            User user = users.get(i);
            if (Objects.isNull(user)) {
                continue;
            }
            String formatted = String.format(content, user.getNickname(), article.getImageName(), article.getId(), article.getContent());
            articlesComponent.append(formatted);
        }
        return String.format(originHtml, articlesComponent);
    }
}
