package codesquad.webserver.staticresouce;

import codesquad.webserver.annotation.Component;
import codesquad.webserver.httpresponse.HttpResponseBuilder;

@Component
public class DefaultStaticResourceResolver implements StaticResourceResolver {

    @Override
    public boolean isStaticResource(String path) {
        String extension = getExtension(path);
        return HttpResponseBuilder.MIME_TYPES.containsKey(extension);
    }

    private String getExtension(String path) {
        int dotIndex = path.lastIndexOf('.');
        return (dotIndex == -1) ? "" : path.substring(dotIndex + 1).toLowerCase();
    }
}
