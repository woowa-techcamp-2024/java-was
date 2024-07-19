package codesquad.servlet.handler.api;

import codesquad.servlet.execption.ServerException;
import codesquad.servlet.handler.Handler;
import codesquad.webserver.http.HttpMediaType;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.http.HttpStatus;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(ImageHandler.class);

    private static final String IMAGE_DIRECTORY = System.getProperty("user.home") + "/images/";

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        logger.debug("ImageHandler START");

        Map<String, String> queryStrings = request.getQueryStrings();
        String imagePath = queryStrings.get("path");
        if (imagePath == null || imagePath.isEmpty()) {
            return;
        }

        File imageFile = new File(IMAGE_DIRECTORY, new File(imagePath).getName());
        if (!imageFile.exists()) {
            return;
        }

        try (FileInputStream fis = new FileInputStream(imageFile)) {
            response.setContentType(HttpMediaType.fromName(getContentType(imagePath)));
            response.setBody(fis.readAllBytes());
        } catch (IOException e) {
            throw new ServerException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.debug("ImageHandler END");
    }

    private String getContentType(String filename) {
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filename.endsWith(".png")) {
            return "image/png";
        } else if (filename.endsWith(".gif")) {
            return "image/gif";
        }
        return "application/octet-stream";
    }
}
