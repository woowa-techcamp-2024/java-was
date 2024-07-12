package util;

import http.startline.UrlPath;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
            throw new FileNotFoundException("File not found");
        }

        return fileContent;
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

    private static String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex == -1) {
            return ""; // 확장자가 없는 경우 빈 문자열 반환
        }
        return fileName.substring(lastIndex + 1).toLowerCase();
    }

}
