package codesquad.webserver;

import java.io.File;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileReader {
    private static Logger logger = LoggerFactory.getLogger(FileReader.class);
    private static final String STATIC_DIRECTORY = "static";

    public File read(String requestPath) {
        String fileName = requestPath.equals("/") ? "index.html" : requestPath.substring(1);

        URL resource = getClass().getClassLoader().getResource(STATIC_DIRECTORY + "/" + fileName);

        if (resource == null) {
            logger.error("File not found: " + fileName);
            throw new RuntimeException("File not found: " + fileName);
        }

        logger.info("Reading file: " + resource.getPath());

        File file = new File(resource.getFile());

        if (!file.exists() || !file.isFile()) {
            logger.error("File not found: " + resource.getFile());
            throw new RuntimeException("File not found: " + resource.getFile());
        }

        logger.info("File found: " + file.getPath());
        return file;

    }
}
