package codesquad.handler;

import codesquad.database.UserH2Database;
import codesquad.file.FileReader;
import codesquad.http.HttpStatus;
import codesquad.http.request.HttpRequest;
import codesquad.http.response.HttpResponse;
import codesquad.model.User;

import java.util.List;

public class UserListHandler implements Handler {

    private final UserH2Database userH2Database = new UserH2Database();

    @Override
    public HttpResponse handle(HttpRequest request) throws RuntimeException {
        if(request.method().equals("GET")) {
            return doGet(request);
        }
        return new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED, "text", new byte[0]);
    }

    public HttpResponse doGet(HttpRequest request) throws RuntimeException {
        byte[] content = FileReader.getContent("/user/list.html");
        List<User> users = userH2Database.getAllUser();
        return new HttpResponse(HttpStatus.OK, "html", replace(new String(content), users).getBytes());
    }

    private String replace(String html, List<User> users){
        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            sb.append("""
                    <tr>
                        <td>""").append(user.getUserId()).append("</td>\n");
            sb.append("""
                    <td>""").append(user.getName()).append("</td>\n");
            sb.append("""
                    </tr>
                    """);
        }
        return html.replace("{{users}}", sb.toString());

    }
}
