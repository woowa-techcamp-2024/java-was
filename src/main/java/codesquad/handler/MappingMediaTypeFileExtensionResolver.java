package codesquad.handler;

import codesquad.http.HttpMediaType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MappingMediaTypeFileExtensionResolver {

    private final Map<String, HttpMediaType> mediaTypes = new ConcurrentHashMap<>();

    public MappingMediaTypeFileExtensionResolver() {
        mediaTypes.put("html", HttpMediaType.TEXT_HTML);
        mediaTypes.put("css", HttpMediaType.TEXT_CSS);
        mediaTypes.put("json", HttpMediaType.APPLICATION_JSON);
        mediaTypes.put("ico", HttpMediaType.IMAGE_X_ICON);
        mediaTypes.put("png", HttpMediaType.IMAGE_PNG);
        mediaTypes.put("jpeg", HttpMediaType.IMAGE_JPEG);
        mediaTypes.put("svg", HttpMediaType.IMAGE_SVG);
    }

    public HttpMediaType resolve(String extension) {
        if (mediaTypes.containsKey(extension)) {
            return mediaTypes.get(extension);
        }
        return HttpMediaType.APPLICATION_OCTET_STREAM;
    }

}
