package codesquad.application.session;

import codesquad.webserver.http.HttpHeader;
import codesquad.webserver.http.HttpRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CookieExtractor {
    public static String getSid(HttpRequest request){
        HttpHeader httpHeader = request.getHttpHeader();
        String cookie = httpHeader.getValue("Cookie");
        Pattern pattern = Pattern.compile("sid=([^;]+)");
        Matcher matcher = pattern.matcher(cookie);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
