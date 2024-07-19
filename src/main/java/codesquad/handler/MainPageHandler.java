package codesquad.handler;

import codesquad.authenticate.Authenticator;
import codesquad.database.PostH2Database;
import codesquad.database.SessionDatabase;
import codesquad.file.FileReader;
import codesquad.http.HttpStatus;
import codesquad.http.request.HttpRequest;
import codesquad.http.response.HttpResponse;
import codesquad.model.Post;
import codesquad.model.User;

import java.util.List;
import java.util.Optional;

public class MainPageHandler implements Handler {

    private static final SessionDatabase sessionDatabase = new SessionDatabase();
    private static final PostH2Database postH2Database = new PostH2Database();

    @Override
    public HttpResponse handle(HttpRequest request) throws RuntimeException {
        if (request.method().equals("GET")) {
            return doGet(request);
        }
        return new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED, "text", new byte[0]);
    }

    public HttpResponse doGet(HttpRequest request) throws RuntimeException {
        if (Authenticator.isLogin(request)) {
            Optional<User> user = sessionDatabase.findUserBySessionId(Authenticator.getSid(request.headers()));
            if (user.isPresent()) {
                byte[] content = FileReader.getContent("/login_index.html");
                String s = new String(content);
                s = s.replace("{{username}}", user.get().getName());
                s = writePost(s);
                return new HttpResponse(HttpStatus.OK, "html", s.getBytes());
            }
        }

        byte[] content = FileReader.getContent("/index.html");
        String s = new String(content);
        s = writePost(s);
        return new HttpResponse(HttpStatus.OK, "html", s.getBytes());
    }

    private String writePost(String s) {
        StringBuilder sb = new StringBuilder();
        List<Post> posts = postH2Database.getAllPost();
        for (Post post : posts) {
            sb.append("""
                    <div class="post">
                      <div class="post__account">
                        <img class="post__account__img" />
                        <p class="post__account__nickname">""");
            sb.append(post.getAuthor().getName());
            sb.append("""
                    </p>
                    </div>
                    <img class="post__img" />
                    <div class="post__menu">
                      <ul class="post__menu__personal">
                        <li>
                          <button class="post__menu__btn">
                            <img src="./img/like.svg" />
                          </button>
                        </li>
                        <li>
                          <button class="post__menu__btn">
                            <img src="./img/sendLink.svg" />
                          </button>
                        </li>
                      </ul>
                      <button class="post__menu__btn">
                        <img src="./img/bookMark.svg" />
                      </button>
                    </div>
                    <p class="post__article">
                    """);
            sb.append(post.getContent());
            sb.append("""
                    </p>
                    </div>
                    """);
        }
        return s.replace("{{body}}", sb.toString());
    }

}
