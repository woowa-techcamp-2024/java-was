package codesquad.configuration;

import codesquad.domain.ArticleStorage;
import codesquad.domain.CommentStorage;
import codesquad.domain.UserStorage;
import codesquad.servlet.SessionStorage;
import codesquad.servlet.handler.HandlerMapper;
import codesquad.servlet.handler.api.AllUserInfoHandler;
import codesquad.servlet.handler.api.ArticleCommentWriteHandler;
import codesquad.servlet.handler.api.ArticleListHandler;
import codesquad.servlet.handler.api.ArticleWriteHandler;
import codesquad.servlet.handler.api.UserInfoHandler;
import codesquad.servlet.handler.api.UserLoginHandler;
import codesquad.servlet.handler.api.UserLogoutHandler;
import codesquad.servlet.handler.api.UserRegistrationHandler;
import codesquad.webserver.http.HttpMethod;

public class HandlerConfiguration {

    public static void handlerMapperConfig(UserStorage userStorage,
                                           SessionStorage sessionStorage,
                                           ArticleStorage articleStorage,
                                           CommentStorage commentStorage
    ) {
        UserRegistrationHandler userRegistrationHandler = new UserRegistrationHandler(userStorage);

        UserLoginHandler userLoginHandler = new UserLoginHandler(userStorage, sessionStorage);

        UserLogoutHandler userLogoutHandler = new UserLogoutHandler(sessionStorage);

        UserInfoHandler userInfoHandler = new UserInfoHandler(sessionStorage);

        AllUserInfoHandler allUserInfoHandler = new AllUserInfoHandler(userStorage);

        ArticleWriteHandler articleWriteHandler = new ArticleWriteHandler(sessionStorage, articleStorage);

        ArticleListHandler articleListHandler = new ArticleListHandler(articleStorage, userStorage, commentStorage);

        ArticleCommentWriteHandler articleCommentWriteHandler = new ArticleCommentWriteHandler(articleStorage,
                sessionStorage, commentStorage);

        // TODO 생성자로 핸들러, 메서드, URL 정보를 클래스로 만들어서 그거에 대한 리스트(HttpMethod, Map<String, Handler>)를 생성자로 받음
        HandlerMapper handlerMapper = HandlerMapper.getInstance();
        handlerMapper.addMapping(HttpMethod.POST, "/user/create", userRegistrationHandler);
        handlerMapper.addMapping(HttpMethod.POST, "/user/login", userLoginHandler);
        handlerMapper.addMapping(HttpMethod.GET, "/user/logout", userLogoutHandler);
        handlerMapper.addMapping(HttpMethod.GET, "/api/user/info", userInfoHandler);
        handlerMapper.addMapping(HttpMethod.GET, "/api/user/list", allUserInfoHandler);
        handlerMapper.addMapping(HttpMethod.POST, "/article", articleWriteHandler);
        handlerMapper.addMapping(HttpMethod.GET, "/api/articles/list", articleListHandler);
        handlerMapper.addMapping(HttpMethod.POST, "/comment", articleCommentWriteHandler);
    }

}
