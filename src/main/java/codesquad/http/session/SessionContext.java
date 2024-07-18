package codesquad.http.session;

import codesquad.model.User;

public record SessionContext(String sessionId, User user) {
}
