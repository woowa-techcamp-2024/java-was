package handler;

import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import util.StaticPage;

public class LoginPageHandler extends MyHandler{
    @Override
    void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        ResponseValueSetter.redirect(httpRequest, httpResponse, StaticPage.loginPage);
    }
}
