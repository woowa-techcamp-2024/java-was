package codesquad.resource;

public class Resource {

    private final byte[] content;
    private final String extension;

    private Resource(byte[] content, String extension) {
        this.content = content;
        this.extension = extension;
    }

    public static Resource of(String fileName, byte[] content) {
        String extension = "";
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot != -1) {
            extension = fileName.substring(lastIndexOfDot + 1);
        }
        return new Resource(content, extension);
    }

    public byte[] getContent() {
        return content;
    }

    public String getExtension() {
        return extension;
    }

    public int getContentLength() {
        return content.length;
    }

}
