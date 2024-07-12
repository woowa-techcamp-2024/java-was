package codesquad.handler;

import codesquad.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class HtmlTransformer {

    public static String replaceUserHeader(String originHtml, User user) {
        try (BufferedReader reader = new BufferedReader(new StringReader(originHtml))) {
            StringBuilder replacedHtml = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("id=\"headerButton\"")) {
                    line = line.replace("로그인", "로그아웃");
                    line = line.replace("/login", "/user/logout");
                }
                if (line.contains("id=\"headerLabel\"")) {
                    line = line.replace("회원 가입", user.getNickname());
                    line = line.replace("href=\"/registration\"", "");
                }
                replacedHtml.append(line);
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
}
