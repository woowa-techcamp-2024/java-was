package codesquad.authenticate;

import codesquad.database.SessionDatabase;
import codesquad.http.request.HttpRequest;
import codesquad.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Authenticator {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final static SessionDatabase sessionDatabase = new SessionDatabase();
    private final static List<String> AUTH_REQUIRED_PATHS = List.of("/user/list", "/user/list.html", "/user/logout", "/write", "/write-tmp.html");

    private Authenticator(){
    }

    public static boolean auth(HttpRequest request) {
        if(AUTH_REQUIRED_PATHS.stream().noneMatch(path -> path.equals(request.path()))) return true;
        return isLogin(request);
    }

    public static boolean isLogin(HttpRequest request) {
        String sid = getSid(request.headers());
        if(sid != null) {
            Optional<User> user = sessionDatabase.findUserBySessionId(sid);
            return user.isPresent();
        }
        return false;
    }

    public static String getSid(Map<String, String> headers) {
        if(headers.get("Cookie") == null)
            return null;
        return headers.get("Cookie").split("=")[1];
    }

}
