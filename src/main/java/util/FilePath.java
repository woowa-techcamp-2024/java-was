package util;

import java.io.File;

public class FilePath {

    private final String path;

    public FilePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        int lastSlashIndex = path.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            return path.isEmpty() ? "" : path;
        }
        String fileName = path.substring(lastSlashIndex + 1);
        return fileName.isEmpty() ? "" : fileName;
    }

    public FilePath join(String otherPath) {
        // 경로 결합을 위한 File 객체 생성
        File combinedPath = new File(path, otherPath);
        return new FilePath(combinedPath.getPath());
    }

    private String getExtension() {
        int lastDotIndex = path.lastIndexOf('.');
        int lastSlashIndex = path.lastIndexOf('/');

        // 마지막 슬래시 뒤에 점(.)이 없는 경우 빈 문자열 반환
        if (lastDotIndex == -1 || lastDotIndex < lastSlashIndex) {
            return "";
        }
        return path.substring(lastDotIndex + 1);
    }
}
