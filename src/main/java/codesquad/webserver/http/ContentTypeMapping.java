package codesquad.webserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ContentTypeMapping {
    private ContentTypeMapping(){}
    private static final Map<String, String> contentTypes = new HashMap<>();
    static{
        contentTypes.put(".html", "text/html;charset=utf-8");
        contentTypes.put(".css", "text/css");
        contentTypes.put(".js", "text/javascript");
        contentTypes.put(".ico", "image/x-icon");
        contentTypes.put(".svg", "image/svg+xml");
        contentTypes.put(".png", "image/png");
        contentTypes.put(".jpg", "image/jpeg");
        contentTypes.put(".jpeg", "image/jpeg");
        contentTypes.put(".gif", "image/gif");
        contentTypes.put(".json", "application/json");
        contentTypes.put(".xml", "application/xml");
        contentTypes.put(".pdf", "application/pdf");
        contentTypes.put(".zip", "application/zip");
        contentTypes.put(".mp3", "audio/mpeg");
        contentTypes.put(".mp4", "video/mp4");
    }
    public static String getContentType(String fileExtension){
        return contentTypes.get(fileExtension);
    }

    public static Set<String> getFileExtensions(){
        return contentTypes.keySet();
    }
}
