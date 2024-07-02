package codesquad.http;

public class HttpParser {
    public static String[] parseStartLine(final String line) {
        return line.split(" ");
    }

    public static String[] parseHeader(final String line) {
        return line.split(": ", 2);
    }
}
