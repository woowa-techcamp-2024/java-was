package handler;

import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import model.User;
import repository.UserRepository;
import session.Session;
import util.RequestContext;

public class UserListHandler extends MyHandler {

    @Override
    void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        Optional<User> user = RequestContext.current().getSession()
            .flatMap(session -> Optional.ofNullable(
                (User) session.getAttribute(Session.USER)
            ));

        if (user.isEmpty()) {
            ResponseValueSetter.redirect(httpRequest, httpResponse, "/index.html");
            return;
        }

        String userListHtml = generateUserListHtml(UserRepository.getInstance().findAll());

        ResponseValueSetter.success(httpResponse,
            userListHtml.getBytes(StandardCharsets.UTF_8));
    }

    private String generateUserListHtml(User[] users) {
        StringBuilder userList = new StringBuilder();
        userListHead(userList);

        for (User user : users) {
            userList.append("<tr>")
                .append("<td>").append(user.getUserId()).append("</td>")
                .append("<td>").append(user.getName()).append("</td>")
                .append("</tr>");
        }

        userList.append("</tbody>")
            .append("</table>")
            .append("</div>")
            // HTML 종료 부분
            .append("</body>")
            .append("</html>");

        return userList.toString();
    }


    private void userListHead(StringBuilder userList) {
        // HTML 시작 부분
        userList.append("<!DOCTYPE html>")
            .append("<html lang=\"en\">")
            .append("<head>")
            .append("<meta charset=\"UTF-8\">")
            .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
            .append("<title>사용자 정보</title>")
            .append("<style>")
            .append(
                "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; height: 100vh; }")
            .append(
                ".container { width: 80%; margin: 20px auto; background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }")
            .append("h2 { text-align: center; margin-bottom: 20px; color: #333; }")
            .append("table { width: 100%; border-collapse: collapse; }")
            .append("table, th, td { border: 1px solid #ddd; }")
            .append("th, td { padding: 12px; text-align: left; }")
            .append("th { background-color: #f2f2f2; }")
            .append("tr:nth-child(even) { background-color: #f9f9f9; }")
            .append("tr:hover { background-color: #f1f1f1; }")
            .append("</style>")
            .append("</head>")
            .append("<body>")
            .append("<div class=\"container\">")
            .append("<h2>사용자 정보</h2>")
            .append("<table>")
            .append("<thead>")
            .append("<tr>")
            .append("<th>User ID</th>")
            .append("<th>Name</th>")
            .append("</tr>")
            .append("</thead>")
            .append("<tbody>");
    }
}
