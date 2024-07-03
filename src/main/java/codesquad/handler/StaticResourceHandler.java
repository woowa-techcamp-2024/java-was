package codesquad.handler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StaticResourceHandler {

    public static final String STATIC_PATH = "src/main/resources/static/";

    public static String getFileContents(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}
