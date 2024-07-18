package handler;

import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import java.io.IOException;
import util.StaticPage;

public class RegistrationHandler extends MyHandler{

    @Override
    void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        ResponseValueSetter.redirect(httpRequest, httpResponse, StaticPage.registerPage);
    }
}
