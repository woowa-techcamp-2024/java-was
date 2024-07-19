package codesquad.servlet.handler.resource;

import codesquad.servlet.execption.ClientException;
import codesquad.webserver.http.HttpStatus;
import java.io.IOException;
import java.io.InputStream;

public class StaticResourceReader {

    public static final String STATIC_PATH = "static";

    private StaticResourceReader() {
    }

    private static class SingletonHolder {
        private static final StaticResourceReader INSTANCE = new StaticResourceReader();
    }

    public static StaticResourceReader getInstance() {
        return StaticResourceReader.SingletonHolder.INSTANCE;
    }

    public byte[] getFileContents(String path) {
        String targetPath = STATIC_PATH + path;
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(targetPath)) {
            if (resourceAsStream == null) {

                throw new ClientException("정적 리소스를 찾을 수 없습니다. targetPath = " + targetPath, HttpStatus.NOT_FOUND);
            }
            return resourceAsStream.readAllBytes();
        } catch (IOException e) {
            throw new ClientException("정적 리소스를 찾을 수 없습니다. targetPath = " + targetPath, HttpStatus.NOT_FOUND);
        }
    }
}
