package http;

import http.startline.StartLine;

public class Http {
    // TODO: 인터페이스로 변경하는 것을 고려해볼 것
    private StartLine startLine;
    private final Header header;
    private byte[] body;

    protected Http(StartLine startLine, Header header, byte[] body) {
        this.startLine = startLine;
        this.header = header;
        this.body = body;
    }

    public StartLine getStartLine() {
        return startLine;
    }

    public Header getHeader() {
        return header;
    }

    public void setStartLine(StartLine startLine) {
        this.startLine = startLine;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
