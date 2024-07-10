package handler;

import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import java.io.IOException;

public class RegistrationHandler extends MyHandler{

    @Override
    void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        ResponseValueSetter.redirect(httpRequest, httpResponse, "/registration/index.html");
    }
}
