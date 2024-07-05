package codesquad.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public final class ResourcesReader {

    private static final String RESOURCES_PATH = "/static";
    private static final Logger log = LoggerFactory.getLogger(ResourcesReader.class);

    private ResourcesReader() {
    }

    public static Optional<Resource> readResource(String path) {
        path = checkPath(path);
        String fullPath = RESOURCES_PATH + path;
        if (!path.contains(".")) {
            return Optional.of(Resource.directory(path));
        }

        try (InputStream inputStream = ResourcesReader.class.getResourceAsStream(fullPath)) {
            if (inputStream == null) {
                log.error("[ERROR] 파일을 읽을 수 없습니다: {}", fullPath);
                return Optional.empty();
            }

            byte[] content = inputStream.readAllBytes();
            Resource resource = Resource.file(path, content);
            return Optional.of(resource);
        } catch (IOException e) {
            log.error("[ERROR] 파일을 읽을 수 없습니다.", e);
            return Optional.empty();
        }
    }

    private static String checkPath(String path) {
        if (path.contains("static")) {
            path = path.replace("static", "");
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        if (!path.isEmpty() && !path.startsWith("/")) {
            path = "/" + path;
        }
        return path;
    }

}
