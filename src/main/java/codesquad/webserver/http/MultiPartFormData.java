package codesquad.webserver.http;

import java.util.Arrays;

public class MultiPartFormData {

    private String name;
    private String filename;
    private String contentType;
    private byte[] content;

    public MultiPartFormData(String name, String filename, String contentType, byte[] content) {
        this.name = name;
        this.filename = filename;
        this.contentType = contentType;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public byte[] getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "MultiPartFormData{" +
                "name='" + name + '\'' +
                ", filename='" + filename + '\'' +
                ", contentType='" + contentType + '\'' +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
