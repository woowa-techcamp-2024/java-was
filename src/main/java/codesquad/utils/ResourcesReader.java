package codesquad.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public final class ResourcesReader {

    private static final String RESOURCES_PATH = "src/main/resources/";
    private static final Logger log = LoggerFactory.getLogger(ResourcesReader.class);

    private ResourcesReader() {
    }

    public static Optional<String> readResource(String path) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(RESOURCES_PATH + path));
            return Optional.of(new String(bytes, "UTF-8"));
        } catch (IOException e) {
            log.error("[ERROR] 파일을 읽을 수 없습니다.", e);
            return Optional.empty();
        }
    }

}
