package codesquad.server.processor.message;

import codesquad.http.HttpResponse;
import codesquad.http.element.ResponseBody;
import codesquad.http.exception.client.Http404Exception;
import codesquad.server.processor.template.TemplateEngine;

import java.io.*;
import java.net.URI;

import static codesquad.utils.StringUtil.CRLF;

public class HttpGenerator {

    public static byte[] generate(HttpResponse httpResponse) throws IOException {
        String startLine = httpResponse.getResponseStartLine().toString();
        String headers = httpResponse.getHttpHeaders().toString();

        byte[] headerLines = (startLine + headers + CRLF).getBytes();
        byte[] bodyLines = generateFileBody(httpResponse.getResponseBody());
        return concatenate(headerLines, bodyLines);
    }

    public static byte[] generateException(HttpResponse httpResponse) throws IOException {
        String startLine = httpResponse.getResponseStartLine().toString();
        String headers = httpResponse.getHttpHeaders().toString();

        byte[] headerLines = (startLine + headers + CRLF).getBytes();
        byte[] bodyLines = "<h1>%s</h1>".formatted(httpResponse.getResponseStartLine().status()).getBytes();
        return concatenate(headerLines, bodyLines);
    }

    private static byte[] generateFileBody(ResponseBody body) throws IOException {
        if (body == null) return new byte[0];
        byte[] result;
        URI uri = body.getUri();

        InputStream fileInputStream = null;
        if (uri.getPath().startsWith("/upload")) {
            String filePath = System.getProperty("user.dir") + uri.getPath();
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                fileInputStream = new FileInputStream(file);
            } else {
                throw new Http404Exception();
            }
        }
        else {
            fileInputStream = HttpGenerator.class.getResourceAsStream("/static" + uri.getPath());
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            if (fileInputStream == null) throw new Http404Exception();

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            result = byteArrayOutputStream.toByteArray();
        }
        if (body.getUri().getPath().endsWith(".html")) result = TemplateEngine.convert(result, body.getBodyMap().get("id"));
        return result;
    }

    private static byte[] concatenate(byte[]... arrays) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            for (byte[] array : arrays) {
                outputStream.write(array);
            }
            return outputStream.toByteArray();
        }
    }
}
