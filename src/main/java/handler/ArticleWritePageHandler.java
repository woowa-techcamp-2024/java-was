package handler;

import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import org.slf4j.Logger;
import util.LoggerUtil;
import util.RequestContext;
import util.StaticPage;

public class ArticleWritePageHandler extends MyHandler {
    private static final Logger logger = LoggerUtil.getLogger();

    @Override
    void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        // login
        RequestContext.current().getSession().ifPresentOrElse(
            session -> {
                if (session.getAttribute("userId") == null) {
                    ResponseValueSetter.redirect(httpRequest, httpResponse, StaticPage.loginPage);
                }
                ResponseValueSetter.redirect(httpRequest, httpResponse, StaticPage.articleWritePage);
            },
            () -> ResponseValueSetter.redirect(httpRequest, httpResponse, StaticPage.loginPage)
        );
    }
}
