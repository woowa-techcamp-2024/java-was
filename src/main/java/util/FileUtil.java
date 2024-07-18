package util;

public class FileUtil {
    public static String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex == -1) {
            return ""; // 확장자가 없는 경우 빈 문자열 반환
        }
        return fileName.substring(lastIndex + 1).toLowerCase();
    }
}
