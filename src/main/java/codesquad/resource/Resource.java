package codesquad.resource;

import java.io.File;
import java.util.Arrays;

public class Resource {

    private final byte[] content;
    private final String extension;
    private final String fileName;
    private final String path;
    private final ResourceType type;

    private Resource(byte[] content, String extension, String fileName, String path, ResourceType type) {
        this.content = content;
        this.extension = extension;
        this.fileName = fileName;
        this.path = path;
        this.type = type;
    }

    public static Resource of(File file, byte[] content) {
        String extension = "";
        String fileName = file.getName();
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot != -1) {
            extension = fileName.substring(lastIndexOfDot + 1);
        }
        String path = file.getPath();
        return new Resource(content, extension, fileName, path, file.isFile()
                ? ResourceType.FILE : ResourceType.DIRECTORY);
    }

    public byte[] getContent() {
        return content;
    }

    public String getExtension() {
        return extension;
    }

    public boolean isFile() {
        return type == ResourceType.FILE;
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }

    public int getContentLength() {
        return content.length;
    }

    enum ResourceType {
        FILE, DIRECTORY
    }

    @Override
    public String toString() {
        return "Resource{" +
                "content=" + Arrays.toString(content) +
                ", extension='" + extension + '\'' +
                ", fileName='" + fileName + '\'' +
                ", path='" + path + '\'' +
                ", type=" + type +
                '}';
    }

}
