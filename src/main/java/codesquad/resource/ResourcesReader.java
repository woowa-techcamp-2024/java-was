package codesquad.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

public final class ResourcesReader {

    private static final String RESOURCES_PATH = "src/main/resources/";
    private static final Logger log = LoggerFactory.getLogger(ResourcesReader.class);

    private ResourcesReader() {
    }

    public static Optional<Resource> readResource(String path) {
        File file = new File(RESOURCES_PATH + path);
        try (FileInputStream inputStream = new FileInputStream((file))) {
            byte[] content = new byte[(int) file.length()];
            inputStream.read(content);
            return Optional.of(Resource.of(file.getName(), content));
        } catch (IOException e) {
            log.error("[ERROR] 파일을 읽을 수 없습니다.", e);
            return Optional.empty();
        }
    }

}
