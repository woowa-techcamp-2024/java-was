package codesquad.webserver.dispatcher.view;

import codesquad.webserver.annotation.Component;
import codesquad.webserver.httpresponse.HttpResponse.Header;
import codesquad.webserver.session.cookie.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RedirectView implements View {

    @Override
    public ViewResult render(Map<String, ?> model) {
        String url = (String) model.get("redirectUrl");
        if (url == null) {
            throw new IllegalStateException("redirectUrl cannot be null");
        }

        List<HttpCookie> cookies = extractCookies(model);
        List<Header> headers = new ArrayList<>();
        headers.add(new Header("Location", url));

        return new ViewResult(new byte[0], cookies, headers, 302);
    }

    private List<HttpCookie> extractCookies(Map<String, ?> model) {
        List<HttpCookie> cookies = new ArrayList<>();
        if (model.containsKey("cookies")) {
            Object cookiesObj = model.get("cookies");
            if (cookiesObj instanceof List) {
                for (Object cookie : (List) cookiesObj) {
                    if (cookie instanceof HttpCookie) {
                        cookies.add((HttpCookie) cookie);
                    }
                }
            }
        }
        return cookies;
    }
}
