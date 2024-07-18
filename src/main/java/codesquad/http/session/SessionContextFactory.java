package codesquad.http.session;

import codesquad.database.H2Config;
import codesquad.database.UserRepository;
import codesquad.http.HttpRequest;

public class SessionContextFactory {

    private static SessionContextFactory instance;

    private final SessionManager sessionManager = SessionManager.getInstance();
    private final UserRepository userRepository = UserRepository.getInstance(H2Config.standard());

    private SessionContextFactory() {
    }

    public static SessionContextFactory getInstance() {
        if (instance == null) {
            instance = new SessionContextFactory();
        }
        return instance;
    }

    public void createSessionContext(HttpRequest httpRequest) {
        String sessionId = httpRequest.getCookie("sid");
        String foundUserId = sessionManager.findUserId(sessionId)
                .orElse("");
        userRepository.findByUserId(foundUserId)
                .ifPresentOrElse(user -> SessionContextHolder.setContext(sessionId, user),
                        () -> SessionContextHolder.setContext(sessionId, null));
    }
}
