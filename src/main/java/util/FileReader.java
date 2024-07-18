package util;

import static util.FileUtil.getFileExtension;

import http.startline.UrlPath;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.slf4j.Logger;

public class FileReader {

    private final static Logger logger = LoggerUtil.getLogger();
    private final static String rootPath = System.getProperty("user.dir");
    private final static String staticPath = rootPath + "/src/main/resources/static";

    public FileReader(){

    }

    public static byte[] readFileFromUrlPath(String urlPath) throws IOException {
        FilePath filePath = new FilePath(staticPath);
        FilePath join = filePath.join(urlPath);
        logger.info("reading file from {}", join.getPath());
        File file = new File(join.getPath());
        byte[] fileContent = null;
        if (file.exists()) {
            logger.info("file exists");

            // 파일의 모든 바이트 읽기
            fileContent = new byte[(int) file.length()];
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                bis.read(fileContent, 0, fileContent.length);
            }
        } else {
            logger.info("file not exists");
            return null;
        }

        return fileContent;
    }

    public static boolean isFileExists(String urlPath) {
        // 디렉토리가 아닌 파일이 존재하는지 확인
        FilePath filePath = new FilePath(staticPath);
        FilePath join = filePath.join(urlPath);

        logger.info("join path: {}", join.getPath());

        File file = new File(join.getPath());
        return file.exists() && file.isFile();
    }

    public static byte[] readFileFromUrlPath(UrlPath urlPath) throws IOException {
        return readFileFromUrlPath(urlPath.getPath());
    }

    public static String guessContentTypeFromUrlPath(UrlPath urlPath) {
        String extension = getFileExtension(urlPath.getPath());
        return switch (extension) {
            case "html", "htm" -> "text/html";
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "css" -> "text/css";
            case "js" -> "application/javascript";
            case "json" -> "application/json";
            case "svg" -> "image/svg+xml";
            case "ico" -> "image/x-icon";
            default -> "application/octet-stream"; // 기타 파일 형식에 대한 기본 MIME 타입
        };
    }

}
