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
        String extension = getExtension();
        // 마지막 슬래시 뒤에 점(.)이 없으면 빈 문자열 반환
        if (extension.isEmpty()) {
            return "";
        }
        return path.substring(0, path.length() - extension.length() - 1);
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
