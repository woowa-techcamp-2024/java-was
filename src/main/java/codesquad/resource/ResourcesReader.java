package codesquad.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

public final class ResourcesReader {

    private static final String RESOURCES_PATH = "src/main/resources/static";
    private static final Logger log = LoggerFactory.getLogger(ResourcesReader.class);

    private ResourcesReader() {
    }

    public static Optional<Resource> readResource(String path) {
        path = checkPath(path);
        File file = new File(RESOURCES_PATH + path);
        if (file.isDirectory()) {
            return Optional.of(Resource.of(file, null));
        }
        try (FileInputStream inputStream = new FileInputStream((file))) {
            byte[] content = new byte[(int) file.length()];
            inputStream.read(content);
            Resource resource = Resource.of(file, content);
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
        return path;
    }

}
