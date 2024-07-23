package codesquad.was.http;

import static codesquad.was.http.type.CharsetType.UTF_8;

import codesquad.was.exception.InternalServerException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestInputStreamReader {

    private static final byte CR = 13;
    private static final int START_POSITION = 0;

    private final Logger log = LoggerFactory.getLogger(RequestInputStreamReader.class);

    private final BufferedInputStream requestInputStream;
    private final byte[] requestLineBytes;
    private final List<byte[]> headerBytes = new ArrayList<>();


    public RequestInputStreamReader(final BufferedInputStream bufferedInputStream) throws IOException {
        this.requestInputStream = new BufferedInputStream(bufferedInputStream);

        requestLineBytes = readBytesUntilSymbol(CR);
        skipByte(2);

        byte[] currentHeaderBytes = readBytesUntilSymbol(CR);
        while (currentHeaderBytes.length > 1) {
            headerBytes.add(currentHeaderBytes);
            skipByte(2);
            currentHeaderBytes = readBytesUntilSymbol(CR);
        }

        skipByte(2);
    }

    private byte[] readBytesUntilSymbol(final byte symbol) throws IOException {
        byte[] readBytes;
        requestInputStream.mark(START_POSITION);
        int count = countLine(requestInputStream, symbol);
        requestInputStream.reset();

        readBytes = new byte[count];
        requestInputStream.read(readBytes);

        return readBytes;
    }

    private int countLine(final BufferedInputStream bufferedInputStream, final byte specificByte) throws IOException {
        int count = 0;
        while ((bufferedInputStream.read()) != specificByte) {
            count++;
        }
        return count;
    }

    private void skipByte(final int count) throws IOException {
        requestInputStream.skip(count);
    }

    public String readRequestLine() throws UnsupportedEncodingException {
        return new String(requestLineBytes, UTF_8.getCharset());
    }

    public List<String> readHeaders() {
        return headerBytes.stream()
                .map(header -> {
                    try {
                        return new String(header, UTF_8.getCharset());
                    } catch (UnsupportedEncodingException e) {
                        log.error(e.getMessage(), e);
                        throw new InternalServerException();
                    }
                })
                .toList();
    }

    public byte[] readBody(final int contentLength) throws IOException {
        byte[] bytes = new byte[contentLength];
        int read = requestInputStream.read(bytes);
        if (read < contentLength) {
            while (read < contentLength) {
                read += requestInputStream.read(bytes, read, contentLength - read);
            }
        }

        return bytes;
    }
}

