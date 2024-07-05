package codesquad.resource;

import java.util.Arrays;

public class Resource {

    private final byte[] content;
    private final String extension;
    private final String fileName;
    private final ResourceType type;

    private Resource(byte[] content, String extension, String fileName, ResourceType type) {
        this.content = content;
        this.extension = extension;
        this.fileName = fileName;
        this.type = type;
    }

    public static Resource directory(String fileName) {
        return new Resource(null, null, fileName, ResourceType.DIRECTORY);
    }

    public static Resource file(String fileName, byte[] content) {
        String extension = "";
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot != -1) {
            extension = fileName.substring(lastIndexOfDot + 1);
        }
        return new Resource(content, extension, fileName, ResourceType.FILE);
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
                ", type=" + type +
                '}';
    }

}
