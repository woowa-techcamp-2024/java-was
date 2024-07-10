package codesquad.webserver.filereader;

import codesquad.webserver.annotation.Component;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class FileReader {
    private static final Logger logger = LoggerFactory.getLogger(FileReader.class);
    private static final String STATIC_DIRECTORY = "static";
    private static final String BASE_FILE = "index.html";

    public FileResource read(String requestPath) throws FileNotFoundException {
        String fileName = getFileName(requestPath);
        String fullPath = STATIC_DIRECTORY + "/" + fileName;
        InputStream inputStream = getResourceAsStream(fullPath);

        if (inputStream == null && !requestPath.endsWith("/")) {
            fullPath = STATIC_DIRECTORY + "/" + fileName + "/" + BASE_FILE;
            inputStream = getResourceAsStream(fullPath);
        }

        if (inputStream == null && requestPath.endsWith("/")) {
            fullPath = STATIC_DIRECTORY + "/" + fileName + BASE_FILE;
            inputStream = getResourceAsStream(fullPath);
        }

        if (inputStream == null) {
            logger.error("File not found: " + fileName);
            throw new FileNotFoundException("File not found: " + fileName);
        }

        logger.info("File found: " + fullPath);
        return new FileResource(inputStream, getFileNameFromPath(fullPath));
    }

    private InputStream getResourceAsStream(String path) {
        logger.info("Attempting to read: " + path);
        return getClass().getClassLoader().getResourceAsStream(path);
    }

    private String getFileName(String requestPath) {
        return requestPath.equals("/") ? BASE_FILE : requestPath.substring(1);
    }

    private String getFileNameFromPath(String path) {
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }

    public static class FileResource {
        private final InputStream inputStream;
        private final String fileName;

        public FileResource(InputStream inputStream, String fileName) {
            this.inputStream = inputStream;
            this.fileName = fileName;
        }

        public InputStream getInputStream() {
            return inputStream;
        }

        public String getFileName() {
            return fileName;
        }
    }
}
